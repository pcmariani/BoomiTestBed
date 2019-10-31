//import java.util.Properties;
//import java.io.InputStream;
//import java.util.regex.Pattern;
//import java.io.File;
//import com.boomi.execution.ExecutionUtil;
//import com.boomi.execution.ExecutionManager;
//import groovy.transform.Field;

for (int i = 0; i < dataContext.getDataCount(); i++) {
    // TODO INSTRUCTIONS: Execute GroovyTestBed to run this groovy code!


    InputStream is = dataContext.getStream(i)
    //noinspection GroovyAssignabilityCheck
    Properties props = dataContext.getProperties(i) as Properties
    def fileString = inputStreamtoString(is)

//    If the first 2-digits of PO# = '22' then BUSINESS_UNIT = 'PHARM'
//    If the first 5-digits of PO# = 'RPL22' then BUSINESS_UNIT = 'PHARM'
//    else if the first 3-digits of PO# = 'RPL' AND the next 2-digits are not '22',
//      then BUSINESS_UNIT = next 2-digits after 'RPL' (byte 4 and 5 of the PO#).
//    else BUSINESS_UNIT tag = first 2 digits of the PO#

    def bak03 = '2100647112'

    def firstThree = ''
    def firstFive = ''

    def businessUnit = ''

//    if (result === "25" || result === "PH") {
//        busunit = "21";
//    }else if (result === "12"){
//        busunit = "11";
//    }else if (busunit ===null){
//        busunit = result;
//    }


    def firstTwo = ''
    if (bak03.size() > 2) {
        firstTwo = bak03[0..1]
        if (firstTwo == '22') { // First two are 22, so pharm
            businessUnit = 'PHARM'
        }
    }
    if (businessUnit == '' && bak03.size() > 5 && bak03[0..4] == 'RPL22') {
        // first five are RPL22 and we didn't set it yet, pharm
        businessUnit = 'PHARM'
    }
    if (businessUnit == '' && bak03.size() > 5 && bak03[0..2] == 'RPL') {
        // RPL but not RPL22, next two after RPL
        businessUnit = bak03[3..4]
    }
    if (businessUnit == '' && bak03.size() >= 2) {
        // Not RPL, not 22, take first two of BAK03
        businessUnit = bak03[0..1]
    }

    println("firstTwo: " + firstTwo)
    println("firstThree: " + firstThree)
    println("firstFive: " + firstFive)
    println("butag: " + businessUnit)

    dataContext.storeStream(new ByteArrayInputStream(fileString.getBytes("UTF-8")), props)
}


def inputStreamtoString(iStream) {
    Scanner s = new Scanner(iStream);
    s.useDelimiter("\\A");
    String str = s.hasNext() ? s.next() : "";
    s.close();
    return str;
}