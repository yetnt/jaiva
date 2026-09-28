package com.jaiva;

import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.annotation.PublicLibrary;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

@Mojo(
        name = "generate-lib-json",
        defaultPhase = LifecyclePhase.PACKAGE,
        requiresDependencyResolution = ResolutionScope.COMPILE
)
public class GenerateLibJsonMojo extends AbstractMojo {

    /**
     * Package to scan for {@link BaseLibrary} subclasses.
     */
    @Parameter(required = true)
    private String basePackage;

    /**
     * The folder to write the generated JSON files.
     */
    @Parameter(
            defaultValue = "${project.basedir}/jaiva/",
            property = "jaiva.outputDir",
            required = true
    )
    private File outputFolder;

    /**
     * Whether this build should fail if the output directory
     * already contains files.
     */
    @Parameter(
            defaultValue = "true",
            property = "jaiva.failIfNotEmpty"
    )
    private boolean failIfNotEmpty;

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException {

        prepareOutputDirectory();

        load(basePackage, outputFolder);
    }

    private void load(
            String basePackage,
            File outputFolder
    ) throws MojoExecutionException {

        List<String> classpathElements = getClasspathElements();

        try (URLClassLoader projectClassLoader =
                     createProjectClassLoader(classpathElements)) {

            try (ScanResult scanResult = new ClassGraph()
                    .overrideClasspath(classpathElements)
                    .acceptPackages(basePackage)
                    .enableClassInfo()
                    .enableAnnotationInfo()
                    .scan()) {

                ClassInfoList matchingClasses = scanResult
                        .getClassesWithAnnotation(PublicLibrary.class.getName())
                        .filter(classInfo ->
                                classInfo.extendsSuperclass(
                                        BaseLibrary.class.getName()
                                )
                        );


                Class<?> pluginClass = Class.forName(
                        Plugin.class.getName(),
                        true,
                        projectClassLoader
                );

                Method generateToolingJSON =
                        pluginClass.getMethod(
                                "generateToolingJSON",
                                String.class
                        );

                Properties props = new Properties();

                for (ClassInfo classInfo : matchingClasses) {

                    String className = classInfo.getName();

                    AnnotationInfo annotationInfo =
                            classInfo.getAnnotationInfo(
                                    PublicLibrary.class.getName()
                            );

                    if (annotationInfo == null) {
                        continue;
                    }

                    String path =
                            (String) annotationInfo
                                    .getParameterValues()
                                    .getValue("path");

                    getLog().info(
                            "Found PublicLibrary: "
                                    + className
                                    + " [path = "
                                    + path
                                    + "]"
                    );


                    String json = invokeToolingJSON(
                            generateToolingJSON,
                            className
                    );

                    String fileName =
                            sanitizeToFileName(path) + ".json";

                    Path jsonFile =
                            outputFolder
                                    .toPath()
                                    .resolve(fileName);

                    try {
                        Files.writeString(
                                jsonFile,
                                json,
                                StandardCharsets.UTF_8
                        );
                    } catch (IOException e) {
                        throw new MojoExecutionException(
                                "Failed to write tooling JSON for "
                                        + className,
                                e
                        );
                    }

                    props.setProperty(path, fileName);

                    getLog().info(
                            "Generated: " + jsonFile
                    );
                }

                writeFetchProperties(props);
            }
        } catch (IOException e) {
            throw new MojoExecutionException(
                    "Failed to close project classloader",
                    e
            );
        } catch (ClassNotFoundException e) {
            throw new MojoExecutionException(
                    "Could not load project-side Jaiva Plugin",
                    e
            );
        } catch (NoSuchMethodException e) {
            throw new MojoExecutionException(
                    "Project-side Jaiva Plugin does not expose "
                            + "generateToolingJSON(String)",
                    e
            );
        }
    }

    private String invokeToolingJSON(
            Method method,
            String className
    ) throws MojoExecutionException {

        try {
            return (String) method.invoke(
                    null,
                    className
            );

        } catch (IllegalAccessException e) {
            throw new MojoExecutionException(
                    "Could not access project-side Jaiva Plugin",
                    e
            );

        } catch (InvocationTargetException e) {

            Throwable cause = e.getCause();

            throw new MojoExecutionException(
                    "Project-side Jaiva tooling generation failed for "
                            + className,
                    cause
            );
        }
    }

    private URLClassLoader createProjectClassLoader(
            List<String> classpathElements
    ) throws MojoExecutionException {

        try {
            URL[] urls = new URL[classpathElements.size()];

            for (int i = 0; i < classpathElements.size(); i++) {
                urls[i] = new File(
                        classpathElements.get(i)
                ).toURI().toURL();
            }

            /*
             * The platform classloader is deliberately used as the parent.
             *
             * This prevents Maven/plugin classes from leaking into the
             * project-side Jaiva classloader.
             *
             * Project dependencies are supplied explicitly through urls.
             */
            return new URLClassLoader(
                    urls,
                    ClassLoader.getPlatformClassLoader()
            );

        } catch (Exception e) {
            throw new MojoExecutionException(
                    "Failed to create project classloader",
                    e
            );
        }
    }

    private void prepareOutputDirectory()
            throws MojoExecutionException {

        if (!outputFolder.exists()) {

            boolean created = outputFolder.mkdirs();

            if (!created) {
                throw new MojoExecutionException(
                        "Failed to create output directory: "
                                + outputFolder.getAbsolutePath()
                );
            }

            getLog().info(
                    "Created output directory: "
                            + outputFolder.getAbsolutePath()
            );

        } else if (!outputFolder.isDirectory()) {

            throw new MojoExecutionException(
                    "Output path exists but is not a directory: "
                            + outputFolder.getAbsolutePath()
            );
        }

        if (
                failIfNotEmpty
                        && isDirectoryNotEmpty(outputFolder.toPath())
        ) {

            throw new MojoExecutionException(
                    "Output directory is not empty: "
                            + outputFolder.getAbsolutePath()
                            + ". Clean the directory or run with "
                            + "-Djaiva.failIfNotEmpty=false"
            );
        }
    }

    private boolean isDirectoryNotEmpty(
            Path path
    ) throws MojoExecutionException {

        try (Stream<Path> entries = Files.list(path)) {
            return entries.findFirst().isPresent();

        } catch (IOException e) {
            throw new MojoExecutionException(
                    "Failed to inspect output directory: "
                            + path,
                    e
            );
        }
    }

    private void writeFetchProperties(
            Properties props
    ) throws MojoExecutionException {

        props.setProperty(
                "version",
                Main.version
        );

        Path filePath =
                outputFolder
                        .toPath()
                        .resolve("fetch.properties");

        File file = filePath.toFile();

        try {
            file.createNewFile();

        } catch (IOException e) {
            throw new MojoExecutionException(
                    "Could not create fetch.properties",
                    e
            );
        }

        try (OutputStream out =
                     new FileOutputStream(file)) {

            props.store(
                    out,
                    "Jaiva Fetch Configg"
            );

        } catch (IOException e) {
            throw new MojoExecutionException(
                    "An IO Problem occurred",
                    e
            );
        }
    }

    private String sanitizeToFileName(
            String input
    ) {

        if (input == null || input.isBlank()) {
            return "unnamed";
        }

        return input
                .replaceAll("[\\\\/]", "_")
                .replaceAll("[<>:\"|?*]", "")
                .trim()
                .replaceAll("^[.]+", "");
    }

    private List<String> getClasspathElements()
            throws MojoExecutionException {

        try {
            return project.getCompileClasspathElements();

        } catch (
                DependencyResolutionRequiredException e
        ) {

            throw new MojoExecutionException(
                    "Failed to resolve compile classpath",
                    e
            );
        }
    }
}