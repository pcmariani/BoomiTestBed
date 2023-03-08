// package boomitestbed

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.w3c.dom.Document;
// import org.xml.sax.SAXException;

class DataContext {
    // ArrayList dataContextArr
    InputStream is
    Properties props

    // DataContext(ArrayList dataContextArr) {
    //     dataContextArr dataContextArr
    // }
    DataContext(InputStream inputStream, Properties properties){
        is = inputStream
        props = properties
    }

    void storeStream(InputStream inputStream, Properties properties){
    // void storeStream(ArrayList documentPropsArr) {
        // dataContextArr.push(dataContextItem)
        is = inputStream
        props = properties
    }
    int getDataCount(){
        // dataContextArr.size()
        1
    }
    InputStream getStream(int index){
        // dataContextArr[index][0]
        is
    }

    Properties getProperties(int index) {
        // dataContextArr[index][1]
        props
    }

    String printPretty() {
      this.is.text
    }
 

    //     String outString = this.is.text
    // 
    //     if (outString.startsWith("<")) {
    //         def root = new XmlSlurper().parseText(outString)
    //         def out = groovy.xml.XmlUtil.serialize(root).replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim()
    //         return out.toString() + "\n";
    //     }

    //     else if (outString.startsWith("{") || outString.startsWith("[")) {
    //         ObjectMapper mapper = new ObjectMapper();
    //         Object json = mapper.readValue(outString, Object.class);	
    //         return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    //     }
    //     
        // TODO - fix the regex so it doesn't match escaped XML
        // else if (outString =~ /\b([,;\t])/) {
        //     def delimiter
        //     if (outString.startsWith('"')) {
        //         delimiter = (outString =~ /".*?"([,;\t])/)[0][1]
        //     }
        //     else delimiter = (outString =~ /\b([,;\t])/)[0][1]

        //     def printDividers = true
        //     def widths = []
        //     def table = []
        //     def rows = outString.split("\r?\n")

        //     rows.each {
        //         def row = it.split(delimiter)
        //         for (int i = 0; i < row.size(); i++){
        //             if (row[i].length() > widths[i]) widths[i] = row[i].length()
        //         }
        //         table.add(row)
        //     }

        //     def out = new StringBuffer()
        //     table.each {
        //         int j = 0
        //         it.each { 
        //             out << it.padRight(widths[j])
        //             if (printDividers && j < table[0].size() - 1) out << " | "
        //             else (out << "  ")
        //             j++
        //         }
        //         out << '\n'
        //     }
        //     return out.toString()
        // }

        // else {
        //     return outString.toString();
        // }

    // }
}
