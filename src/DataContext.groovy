import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

class DataContext {
    InputStream is
    Properties props

    DataContext(InputStream inputStream, Properties properties){
        is = inputStream
        props = properties
    }

    void storeStream(InputStream inputStream, Properties properties){
        is = inputStream
        props = properties
    }

    int getDataCount(){
        1
    }
    InputStream getStream(int index){
        is
    }
    Properties getProperties(int index) {
        props
    }

    // Would like to make this method more Groovy-y and less Java-y
    String printPretty() throws IOException, ParserConfigurationException, SAXException, TransformerConfigurationException, TransformerException {

        String outString = this.is.text
    
        if (outString.startsWith("<")) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();	
            Document doc = db.parse(new ByteArrayInputStream(outString.getBytes()));

            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

            Writer out = new StringWriter();
            tf.transform(new DOMSource(doc), new StreamResult(out));
            return out.toString();
        }

        else if (outString.startsWith("{") || outString.startsWith("[")) {
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(outString, Object.class);	
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        }
        
        // TODO - fix the regex so it doesn't match escaped XML
        else if (outString =~ /\b([,;\t])/) {
            def delimiter = (outString =~ /\b([,;\t])/)[0][1]
            def printDividers = true
            def widths = []
            def table = []
            def rows = outString.split("\r?\n")

            rows.each {
                def row = it.split(delimiter)
                for (int i = 0; i < row.size(); i++){
                    if (row[i].length() > widths[i]) widths[i] = row[i].length()
                }
                table.add(row)
            }

            def out = new StringBuffer()
            table.each {
                int j = 0
                it.each { 
                    out << it.padRight(widths[j])
                    if (printDividers && j < table[0].size() - 1) out << " | "
                    else (out << "  ")
                    j++
                }
                out << '\n'
            }
            return out.toString()
        }

        else {
            return outString.toString();
        }

    }
}
