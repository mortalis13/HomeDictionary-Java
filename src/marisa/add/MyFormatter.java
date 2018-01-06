package marisa.add;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;


public class MyFormatter extends Formatter {

  static String nl = "\n";
  static String dnl = nl+nl;
  
  
  @Override
  public String format(LogRecord record) {
    StringBuffer sb = new StringBuffer();

    sb.append(record.getSourceClassName()+".");
    sb.append(record.getSourceMethodName()+"()"+nl);
    sb.append("  "+record.getMessage()+dnl);

    return sb.toString();
  }

}
