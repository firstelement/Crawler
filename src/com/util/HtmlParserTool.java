package com.util;

import java.util.HashSet;  
import java.util.Set;  
  
import org.htmlparser.Node;  
import org.htmlparser.NodeFilter;  
import org.htmlparser.Parser;  
import org.htmlparser.filters.NodeClassFilter;  
import org.htmlparser.filters.OrFilter;  
import org.htmlparser.tags.LinkTag;  
import org.htmlparser.util.NodeList;  
import org.htmlparser.util.ParserException;  
  
public class HtmlParserTool {  
	// 获取一个网站上的链接，filter 用来过滤链接
    @SuppressWarnings("serial")  
    public static Set<String> extracLinks(String url, LinkFilter filter) {  
        Set<String> links = new HashSet<String> ();  
        try {  
            Parser parser = new Parser(url);  
            parser.setEncoding("UTF-8");  
         // 过滤 <frame >标签的 filter，用来提取 frame 标签里的 src 属性
            NodeFilter frameFilter = new NodeFilter() {  
                @Override  
                public boolean accept(Node node) {  
                    if(node.getText().startsWith("frame src=")) {  
                        return true;  
                    }  
                      
                    return false;  
                }  
            };  
         // OrFilter 来设置过滤 <a> 标签和 <frame> 标签
            OrFilter linkFilter =   
                new OrFilter(new NodeClassFilter(LinkTag.class), frameFilter);  
            NodeList list = parser.extractAllNodesThatMatch(linkFilter);  
            for(int i=0; i<list.size(); i++) {  
                Node tag = list.elementAt(i);  
                if( tag instanceof LinkTag) {  // <a> 标签
                    LinkTag link = (LinkTag) tag;  
                    String linkUrl = link.getLink();  // URL
                    if(filter.accept(url)) {
                    	if (linkUrl.contains(".com") || linkUrl.contains(".cn")) {
                    		links.add(linkUrl);
						}else {
							links.add(url+linkUrl);
						}
                          
                    } else {  // <frame> 标签
                    	// 提取 frame 里 src 属性的链接，如 <frame src="test.html"/>
                        String frame = tag.getText();  
                        int start  = frame.indexOf("src=");  
                        if( start != -1) {  
                            frame = frame.substring(start);  
                        }  
                        int end = frame.indexOf(" ");  
                        String frameUrl = "";  
                        if(end == -1) {  
                            end = frame.indexOf(">");  
                            if(end-1 > 5) {  
                               frameUrl = frame.substring(5, end - 1);  
                            }  
                        }  
                          
                        if(filter.accept(frameUrl)) {  
                            links.add(frameUrl);  
                        }  
                          
                    }  
                }  
            }  
              
        } catch (ParserException e) {  
            e.printStackTrace();  
            System.out.println("~~"+url);
            
            //如果是因为字符集必须为GBK，则更改成GBK的字符集重新来一次  
            String code = "GBK" ;
            if (e.toString().contains("change from UTF-8 to GBK")) {
            	code = "GBK";
            	}else if (e.toString().contains("change from UTF-8 to GB2312")) {
            		code = "GB2312";
				}
            	 try {  
                     Parser parser = new Parser(url);  
                     parser.setEncoding(code);  
                  // 过滤 <frame >标签的 filter，用来提取 frame 标签里的 src 属性
                     NodeFilter frameFilter = new NodeFilter() {  
                         @Override  
                         public boolean accept(Node node) {  
                             if(node.getText().startsWith("frame src=")) {  
                                 return true;  
                             }  
                               
                             return false;  
                         }  
                     };  
                  // OrFilter 来设置过滤 <a> 标签和 <frame> 标签
                     OrFilter linkFilter =   
                         new OrFilter(new NodeClassFilter(LinkTag.class), frameFilter);  
                     NodeList list = parser.extractAllNodesThatMatch(linkFilter);  
                     for(int i=0; i<list.size(); i++) {  
                         Node tag = list.elementAt(i);  
                         if( tag instanceof LinkTag) {  // <a> 标签
                             LinkTag link = (LinkTag) tag;  
                             String linkUrl = link.getLink();  // URL
                             if(filter.accept(url)) {  
                            	 if (linkUrl.contains(".com") || linkUrl.contains(".cn")) {
                             		links.add(linkUrl);
         						}else {
         							links.add(url+linkUrl);
         						}  
                             } else {  // <frame> 标签
                             	// 提取 frame 里 src 属性的链接，如 <frame src="test.html"/>
                                 String frame = tag.getText();  
                                 int start  = frame.indexOf("src=");  
                                 if( start != -1) {  
                                     frame = frame.substring(start);  
                                 }  
                                 int end = frame.indexOf(" ");  
                                 String frameUrl = "";  
                                 if(end == -1) {  
                                     end = frame.indexOf(">");  
                                     if(end-1 > 5) {  
                                        frameUrl = frame.substring(5, end - 1);  
                                     }  
                                 }  
                                   
                                 if(filter.accept(frameUrl)) {  
                                     links.add(frameUrl);  
                                 }  
                                   
                             }  
                         }  
                     }  
                       
                 } catch (ParserException ee) {  
                     ee.printStackTrace();  
			}
        }    
        return links;  
    }  
}  