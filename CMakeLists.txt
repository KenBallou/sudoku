cmake_minimum_required (VERSION 3.7)
project (sudoku Java)

include (FindJava)
include (FindJNI)
include (UseJava)

file(GLOB_RECURSE sudoku_SOURCE RELATIVE ${CMAKE_CURRENT_SOURCE_DIR} *.java)
set (CMAKE_JNI_TARGET true)
add_jar (sudoku
    SOURCES ${sudoku_SOURCE}
    ENTRY_POINT sudoku.gui.GUI)
install_jar (sudoku ${CMAKE_BINARY_DIR}/bin)
#install_jni_symlink (sudoku ${CMAKE_BINARY_DIR}/bin)
