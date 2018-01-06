
package marisa;

/**
 * this class provides interface to C++ `marisa::Keyset` class.
 */

public class KeysetNative {
  
	public static final float DEFAULT_WAIT = 1.0F;
  long handle;
  
  
  public KeysetNative() {
  	handle = -1;
  }
	
  
	public native void push_back(KeyNative key) throws MarisaException;
	public native void push_back(KeyNative key, byte end_marker) throws MarisaException;
	public void push_back(byte[] str) throws MarisaException {
		push_back(str, 0, str.length, DEFAULT_WAIT);
	}
	public void push_back(byte[] ptr, int length) throws MarisaException {
		push_back(ptr, 0, length, DEFAULT_WAIT);
	}
	public native void push_back(byte[] ptr, int start, int length, float weight) throws MarisaException;
	
	public native KeyNative get(int i) throws MarisaException;
	
	public native long num_keys() throws MarisaException;
	
	public native boolean empty() throws MarisaException;
	public native long size() throws MarisaException;
	public native long total_length() throws MarisaException;
	
	public native void reset() throws MarisaException;
	
	public native void clear() throws MarisaException;
	public native void swap(KeysetNative rhs) throws MarisaException;
	
	public native void dispose();
  
}
