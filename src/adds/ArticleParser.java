package adds;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ArticleParser {
  
  public String rawArticle1;
  public String rawArticle2;
  

  String[] removeTags={
      "trn",
      "com",
      "lang",
      "\\*",
      "!trs",
      "'",
      
//      "i",                // temps
      "c",
      "url",
  };
  
  String[] removeTagsWithContent={
      "s"
  };
  
  String[] styleTags={
      "b",
      "i",
      "u"
  };
  
  String meta="<meta charset='UTF-8'>\n";
  
  String style="<style>"
      + ".label{color: green; font-weight: bold; font-style:italic;}"
      + ".example{color: #666666; font-style: normal; } "
      + ".par-1, .par-2, .par-3, .par-4, .par-5, .par-6, .par-7, .par-6, .par-9{margin:'0 0 2px 0';}"
      + ".par-0{margin:0}"
      + ".par-1{padding-left: 10px; margin-bottom: 5px;}"
      + ".par-2{padding-left: 20px;}"
      + ".par-3{padding-left: 30px;}"
      + ".par-4{padding-left: 40px;}"
      + ".par-5{padding-left: 50px;}"
      + ".par-6{padding-left: 60px;}"
      + ".par-7{padding-left: 70px;}"
      + ".par-8{padding-left: 80px;}"
      + ".par-9{padding-left: 90px;}"
      + ".heading{font-weight:700; margin:'5px 0 5px 10px'; font-size:115%;}"
      + ".dict-name{text-align:right; color: #8e6bc5; font-weight:700; margin:0; padding: '5px 5px 0 0'; border-top: 1px solid #bbbbbb; }"
      + ".article{margin:'5px 0 10px 0'}"
      + "hr{border-color: green; }"
//      + "body{font-family:'汉鼎简黑变'}"
//      + "body{font-family:'MS Song'}"
     // + "body{font-family:'Lucida Sans Unicode'}"
     // + "body{font-family:'Arial Unicode MS'}"
//      + "body{font-family:'Arial'}"
     + "body{font-family:'Arial'; fon-size: 14px}"
      // + "body{font-family:'Tahoma'}"
//      + "body{font-family:'Arial Trebuchet MS'}"
      + "</style>\n";
  
  
  public String parseArticle(String heading, String article){
    String pat, test, res;
    StringBuilder sb=new StringBuilder(article);
    
    pat="\\[/?trn\\]|\\[/?com\\]|\\[s\\].*?\\[/s\\]|\\[/?lang.*?\\]|\\[/?\\*\\]|\\[/?!trs\\]|\\[/?'\\]|\\[/?c.*?\\]|\\[/?t\\]";
    sb=replaceRegex(pat, sb, "");
    
    pat="\\[m(\\d)\\](.*?)\\[ex\\]";
    sb=replaceRegex(pat, sb, "[m$1][ex]$2");
    
    pat="\\[(/?)(sub)\\]";
    sb=replaceRegex(pat, sb, "<$1$2>");
    pat="\\[(/?)(sup)\\]";
    sb=replaceRegex(pat, sb, "<$1$2>");
    
    pat="\\[(/?)(b)\\]";
    sb=replaceRegex(pat, sb, "<$1$2>");
    pat="\\[(/?)(i)\\]";
    sb=replaceRegex(pat, sb, "<$1$2>");
    pat="\\[(/?)(u)\\]";
    sb=replaceRegex(pat, sb, "<$1$2>");
    
    pat="\\[p\\]";
    sb=replaceRegex(pat, sb, "<span class=\"label\">");
    pat="\\[/p\\]";
    sb=replaceRegex(pat, sb, "</span>");
    
    pat="\\[m(\\d)\\]";
    sb=replaceRegex(pat, sb, "<p class=\"par-$1\">");
    pat="\\[m\\]";
    sb=replaceRegex(pat, sb, "<p class=\"par-0\">");
    pat="\\[/m\\]";
    sb=replaceRegex(pat, sb, "</p>");
    
    pat="\\[ex\\]";
    sb=replaceRegex(pat, sb, "<cite class=\"example\">");
    pat="\\[/ex\\]";
    sb=replaceRegex(pat, sb, "</cite>");
    
    pat="\\\\\\[";
    sb=replaceRegex(pat, sb, "[");
    pat="\\\\\\]";
    sb=replaceRegex(pat, sb, "]");
    
    pat="\\[ref\\](.+?)\\[/ref\\]";
    sb=replaceRegex(pat, sb, "<a href='#' title='$1'>$1</a>");
    
    res=sb.toString();
    res=res.trim();
    heading = "<p class=\"heading\">" + heading + "</p>";
    
    String htmlHeader = "<head>\n"+meta+style+"</head>\n";
    res = htmlHeader + heading + res;
    
    res = Jsoup.parse(res).html();
    
    return res;
  }
  
  
  StringBuilder replaceRegex(String pattern, StringBuilder text, String repl){
    String temp;
    Pattern pat=Pattern.compile(pattern);
    Matcher mat=pat.matcher(text);
    
    if(mat.find()){
      temp=mat.replaceAll(repl);
      text.setLength(0);
      text.append(temp);
    }
    
    return text;
  }
  
}
