package com.yuyi.pts.common.cache;

public class ByteDataCache {

    public ByteDataCache(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    private int cacheSize;

    public static byte[] cacheByte = new byte[30000];


    /**
     * 获取点表某一段地址数据
     * @param byteNum 寄存器数量
     * @param address 寄存器起始地址
     * @return byte[]
     */
    public static byte[] getRegister(int byteNum, int address){
        byte[] bytes1 = new byte[byteNum * 2];
        for (int i = 0;i< byteNum;i++){
            bytes1[i] = ByteDataCache.cacheByte[i+((address-1)*2)];
        }
        return  bytes1;
    }


    /**
     * 修改点表某一段地址数据
     * @param byteNum 寄存器数量
     * @param address 寄存器起始地址
     */
    public static void updateRegister(int byteNum, int address, byte [] target){
        System.out.println(cacheByte);
        for(int j=0;j<byteNum*2;j++){
            ByteDataCache.cacheByte[(address-1)*2+j]=target[j];
        }
        System.out.println(cacheByte);
    }

}
