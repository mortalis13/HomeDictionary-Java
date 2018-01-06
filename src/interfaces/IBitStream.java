package interfaces;

public interface IBitStream {
  
  int Read(int len);
  int Read(byte[] dest, int byteCount);
  void Seek(int pos);
  void ToNearestByte();
  int Tell();
  
}
