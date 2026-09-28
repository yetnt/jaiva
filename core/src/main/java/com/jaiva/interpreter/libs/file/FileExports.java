package com.jaiva.interpreter.libs.file;

import com.jaiva.interpreter.libs.BaseLibrary;
import com.jaiva.interpreter.libs.annotation.Exports;
import com.jaiva.interpreter.libs.annotation.JaivaLibrary;

@Exports({FileApi.class, FileQuery.class})
@JaivaLibrary(path = "file")
public class FileExports extends BaseLibrary {
}
