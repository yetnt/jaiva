package com.jaiva;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.artifact.DependencyResolutionRequiredException;

import java.io.File;
import java.util.List;

@Mojo(name = "generate-lib-json", defaultPhase = LifecyclePhase.PACKAGE, requiresDependencyResolution = ResolutionScope.COMPILE)
public class GenerateLibJsonMojo extends AbstractMojo {

    /** Package to scan for {@code BaseLibrary} subclasses. */
    @Parameter(required = true)
    private String basePackage;

    /** Where to write the generated JSON. Defaults to {@code target/jaiva-libs.json}. */
    @Parameter(defaultValue = "${project.build.directory}/jaiva-libs.json")
    private File outputFile;

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException {
        List classpathElements;
        try {
            classpathElements = project.getCompileClasspathElements();
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoExecutionException("Failed to resolve compile classpath", e);
        }

        // build URLClassLoader from classpathElements, scan basePackage for BaseLibrary subclasses,
        // call the SAME generator class the CLI's `generate-lib-json` subcommand already calls.

        // pretend i collected classes that extend BaseLibrary
    }
}
