package weixin.zoo.web;

import com.github.cloudecho.qrcode.ZxingCode;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;

import java.io.IOException;

/**
 * Created by viczhang.zhangz on 2017/5/5.
 */
public class Test {
    public static  void main(String[] args) throws IOException, NotFoundException {
        ZxingCode zxingCode = new ZxingCode();

        Result result = zxingCode.decode("D://345.jpg","utf-8");
        System.out.println(result.getText());

    }
}
