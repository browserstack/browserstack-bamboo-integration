package com.browserstack.bamboo.ci.lib;

import com.browserstack.bamboo.ci.lib.BStackSession;
import com.browserstack.bamboo.ci.lib.JUnitReport;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.DirectoryScanner; 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.HashMap;


public class BStackJUnitSessionMapper {

  private String baseDir;
  private Map<String, String> testSessionMap;
  public List<BStackSession> bStackSessions;
  private static final String pattern = "**/surefire-reports/TEST-*.xml";


  public BStackJUnitSessionMapper(String baseDir, Map<String, String> testSessionMap) {
    this.baseDir = baseDir;
    this.testSessionMap = testSessionMap;
    this.bStackSessions = new ArrayList<BStackSession>();
  }

  public List<BStackSession> parseAndMapJUnitXMLReports() {

    String[] reportFilePaths = findJUnitReports();
    Map<String, Long> testCaseIndices = new HashMap<String, Long>();

    for(String reportFilePath : reportFilePaths) {
      List<JUnitReport> testCases = new ArrayList<JUnitReport>();
      try {

        testCases = parseReport(reportFilePath);

      } catch (IOException e) {
        System.out.println("Error Parsing JUnit Test Reports : " + e.toString());
      }

      for(JUnitReport testCase : testCases) {
        String testCaseName = testCase.fullStrippedName();

        Long testIndex = testCaseIndices.containsKey(testCaseName) ? testCaseIndices.get(testCaseName) : -1L;
        testCaseIndices.put(testCaseName, ++testIndex);
        System.out.println(testCaseName + " / " + testCaseName + " <=> " + testIndex);

        String testId = String.format("%s{%d}", testCaseName, testIndex);
        System.out.println("Searching for testId : " + testId);

        if (testSessionMap.containsKey(testId)) {
            bStackSessions.add(new BStackSession(testCase.fullName(), testSessionMap.get(testId)));
        }
      }
    }

    return bStackSessions;
  }


  private List<JUnitReport> parseReport(String reportPath) throws IOException {

    File reportFile = new File(baseDir + "/" + reportPath);

    Document doc = null;
    List<JUnitReport> testCases = new ArrayList<JUnitReport>();

    try {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(reportFile);
    } catch (Exception e) {
        throw new IOException(e.getMessage());
    }

    if (doc != null) {
      Element documentElement = doc.getDocumentElement();
      NodeList testCaseNodes = documentElement.getElementsByTagName("testcase");

      for (int i = 0; i < testCaseNodes.getLength(); i++) {
          Node n = testCaseNodes.item(i);

          if (n.getNodeType() == Node.ELEMENT_NODE) {
          Element el = (Element) n;

          String name = el.hasAttribute("name") ? el.getAttribute("name") : "";
          String classname = el.hasAttribute("classname") ? el.getAttribute("classname") : "";
          String duration = el.hasAttribute("time") ? el.getAttribute("time") : "";
          testCases.add(new JUnitReport(classname, name, duration));
        }
      }
    }
    return testCases;
  }

  //http://stackoverflow.com/questions/794381/how-to-find-files-that-match-a-wildcard-string-in-java
  private String[] findJUnitReports() {
    DirectoryScanner scanner = new DirectoryScanner();
    scanner.setIncludes(new String[]{pattern});
    scanner.setBasedir(baseDir);
    scanner.setCaseSensitive(false);
    scanner.scan();
    String[] files = scanner.getIncludedFiles();
    return files;
  }
}