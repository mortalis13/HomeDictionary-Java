package interfaces;

public interface IDictionaryDecoder {
  
  void Read(IBitStream bstr);
  void ReadForIndex(IBitStream bstr);
  
  String DecodeHeading(IBitStream bstr, int len);
  String DecodeArticle(IBitStream bstr, int len);
  
  int DecodePrefixLen(IBitStream bstr);
  int DecodePostfixLen(IBitStream bstr);
  
  int ReadReference1(IBitStream bstr);
  int ReadReference2(IBitStream bstr);
  
  String getPrefix();
  
}
