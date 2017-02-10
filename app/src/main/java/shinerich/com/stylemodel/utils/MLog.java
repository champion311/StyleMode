package shinerich.com.stylemodel.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 *  我的日志
 * @author hunk
 *
 */
public final class MLog {
    private static boolean DUG=true;        //调试模式
    private MLog(){}
    
    /**
     *  设置调试
     */
    public static void setDug(boolean isDug){
    	DUG=isDug;
    }

    /**
     * 获取调试模式
     */
    public static boolean isDUG(){
        return DUG;
    }
    
    /**
     *   信息日志
     */
   public static void i(String tag,String msg){
	   if(DUG){
		  Log.i(tag, msg); 
	   }
   }
   
   /**
    *   信息日志
    */
  public static void e(String tag,String msg){
	   if(DUG){
		  Log.e(tag, msg); 
	   }
  }
  
  /**
   *   Toast提示
   */
  public static void showToast(Context context,String msg){
	  if(DUG){
		  Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	  }
  }
  /**
   *   打印异常信息
   */
  public static void printException(Exception ex){
	  if(DUG){
		  ex.printStackTrace();
	  }
  }
}
