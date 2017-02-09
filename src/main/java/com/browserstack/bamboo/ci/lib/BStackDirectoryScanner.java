package com.browserstack.bamboo.ci.lib;
import org.apache.tools.ant.DirectoryScanner; 
import java.lang.IllegalStateException;

public final class BStackDirectoryScanner {

  //http://stackoverflow.com/questions/794381/how-to-find-files-that-match-a-wildcard-string-in-java
  public static String[] findFilesMatchingPattern(String baseDir, String pattern) {
    String[] files = null;

    try {
      DirectoryScanner scanner = new DirectoryScanner();
      scanner.setIncludes(new String[]{pattern});
      scanner.setBasedir(baseDir);
      scanner.setCaseSensitive(false);
      scanner.scan();
      files = scanner.getIncludedFiles();
    } catch(IllegalStateException e) {
      System.out.println("Unable to find  " + baseDir + " for pattern " + pattern);
    }
    
    return files;
  }
}