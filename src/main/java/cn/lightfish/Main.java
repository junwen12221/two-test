package cn.lightfish;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.*;


/**
 * Created by karak on 16-6-5.
 */
public class Main {
    /*
    ab: wrong number of arguments
    Usage: ab [options] [http://]hostname[:port]/path
    Options are:
        -n requests     Number of requests to perform
        -c concurrency  Number of multiple requests to make
        -t timelimit    Seconds to max. wait for responses
        -b windowsize   Size of TCP send/receive buffer, in bytes
        -B address      Address to bind to when making outgoing connections
        -p postfile     File containing data to POST. Remember also to set -T
        -u putfile      File containing data to PUT. Remember also to set -T
        -T content-type Content-type header for POSTing, eg.
                        'application/x-www-form-urlencoded'
                        Default is 'text/plain'
        -v verbosity    How much troubleshooting info to print
        -w              Print out results in HTML tables
        -i              Use HEAD instead of GET
        -x attributes   String to insert as table attributes
        -y attributes   String to insert as tr attributes
        -z attributes   String to insert as td or th attributes
        -C attribute    Add cookie, eg. 'Apache=1234'. (repeatable)
        -H attribute    Add Arbitrary header line, eg. 'Accept-Encoding: gzip'
                        Inserted after all normal header lines. (repeatable)
        -A attribute    Add Basic WWW Authentication, the attributes
                        are a colon separated username and password.
        -P attribute    Add Basic Proxy Authentication, the attributes
                        are a colon separated username and password.
        -X proxy:port   Proxyserver and port number to use
        -V              Print version number and exit
        -k              Use HTTP KeepAlive feature
        -d              Do not show percentiles served table.
        -S              Do not show confidence estimators and warnings.
        -g filename     Output collected data to gnuplot format file.
        -e filename     Output CSV file with percentages served
        -r              Don't exit on socket receive errors.
        -h              Display usage information (this message)
     */
    static String projectPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    static String command = "ab -n {{requests}} -c {{concurrency}} -g {{g}} {{url}}{{datasize}}";
    static String templeateFile = projectPath + "/" + "templeate.hbs";
    static String plotStatement = "\"{{g}}\" using 9 smooth sbezier with lines title \"{{datasize}}k\",\\\n";

    public static Args init() {

        Scanner sc = new Scanner(System.in);
        Args args = new Args();

        System.out.println("请输入总的请求数");
        args.setRequests(sc.nextLine().trim());

        System.out.println("请输入并发数");
        args.setConcurrency(sc.nextLine().trim());

        System.out.println("tsv文件存放文件夹路径,文件名本程序自动命名");
        args.setG(sc.nextLine().replace("\\", "/").trim());

        System.out.println("测试的URL,格式:[http://]hostname[:port]");
        args.setUrl(sc.nextLine().trim());

        return args;
    }

    public static void main(String[] args) throws Exception {

        Args parram = init();
        Handlebars handlebars = new Handlebars();

        Map<String, String> model = new HashMap<String, String>();

        model.put("requests", parram.getRequests());
        model.put("concurrency", parram.getConcurrency());
        model.put("url", parram.getUrl());

        String gPath = parram.getG();
        Template templateCommand = handlebars.compileInline(command);
        String head = FileUtils.readFileToString(new File(templeateFile));
        Template templatePlotStatement = handlebars.compileInline(plotStatement);

        List<String> list = new ArrayList<String>();

        for (int i = 1; i <= 64; i *= 2) {

            String number = Integer.valueOf(i).toString();

            model.put("datasize", number);
            String fileName = gPath + "/" + number + ".tsv";
            model.put("g", fileName);

            String res = templateCommand.apply(model);
            System.out.println(res);

            String n;

            if (i == 1) {
                n = "plot \"" + fileName + "\" using 9 smooth sbezier with lines title \"1k\",\\\n";
            } else if (i == 64) {
                n = "\"" + fileName + "\" using 9 smooth sbezier with lines title \"64k\"";
            } else {
                n = templatePlotStatement.apply(model);
            }
            list.add(n);
        }
        System.out.println();
        String result = head + "\n";

        for (String i : list) {
            result += i;
        }

        System.out.println(result);
    }
}
