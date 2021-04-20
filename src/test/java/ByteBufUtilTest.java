import com.yuyi.pts.common.util.ByteBufUtils;

import java.util.Arrays;

/**
 * description
 *
 * @author greyson
 * @since 2021/4/20
 */
public class ByteBufUtilTest {
    public static void main(String[] args) {
        String hexStr = "0x04";
        byte[] bytes = ByteBufUtils.toByteArray(hexStr);
        System.out.println("第二次：" + Arrays.toString(bytes));
        byte[] bytes1 = ByteBufUtils.hexToByteArray("04");
        System.out.println("第二次：" + Arrays.toString(bytes1));
        byte[] bytes2 = toBytes("04");
        System.out.println("第三次：" + Arrays.toString(bytes2));
        byte[] bytes3 = ByteBufUtils.hexString2Bytes("0x04");
        System.out.println("第三次：" + Arrays.toString(bytes3));
    }

    public static byte[] toBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }

    /*
     * 字符转换为字节
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /*
     * 16进制字符串转字节数组
     */
    public static byte[] hexString2Bytes(String hex) {

        if ((hex == null) || (hex.equals(""))){
            return null;
        }
        else if (hex.length()%2 != 0){
            return null;
        }
        else{
            hex = hex.toUpperCase();
            int len = hex.length()/2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i=0; i<len; i++){
                int p=2*i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p+1]));
            }
            return b;
        }
    }
}


