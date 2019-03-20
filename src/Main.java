import java.util.*;
import java.io.*;
import java.net.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main
{
	public static void main(String[] args)
	{
		String examNum = "";
		System.out.println("-----------会考成绩查询-----------");
		Scanner s = new Scanner(System.in);
		System.out.print("请输入准考证号:");
		examNum = s.next();
		String r = doPost("http://www.heao.gov.cn/datacenter/HK/HKCJ_Show.aspx", "__VIEWSTATE=%2FwEPDwULLTE3MTQ5OTcxMThkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYBBQlTdWJCdXR0b24QE8%2FYM8%2BcevYhAxAN3NNQLHt4Bg%3D%3D&__VIEWSTATEGENERATOR=5A147B19&__EVENTVALIDATION=%2FwEWAwKhyuDkDwK5%2B46yDgLu68ewD66fTXsWTtkE%2BlEEdVXXEdrHNkL3&txtKSH=" + examNum + "&SubButton.x=60&SubButton.y=4");
		if (r.contains("错误的准考证号"))
		{
			System.out.println("查询不到数据！");
		}
		else
		{
			int a = findIndex(r, "<p class=\"toptable\">");
			int b = findIndex(r, "</table>");
			String dealed = r.substring(a, b);
			System.out.println("------查询结果------");
			printGrade(getGrade(dealed));
		}
	}

	public static String doPost(String httpUrl, String param)
	{

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try
		{
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);

            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            os.write(param.getBytes());
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200)
			{

                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, "GBK"));

                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null)
				{
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        }
		catch (MalformedURLException e)
		{
            e.printStackTrace();
        }
		catch (IOException e)
		{
            e.printStackTrace();
        }
		finally
		{
            // 关闭资源
            if (null != br)
			{
                try
				{
                    br.close();
                }
				catch (IOException e)
				{
                    e.printStackTrace();
                }
            }
            if (null != os)
			{
                try
				{
                    os.close();
                }
				catch (IOException e)
				{
                    e.printStackTrace();
                }
            }
            if (null != is)
			{
                try
				{
                    is.close();
                }
				catch (IOException e)
				{
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();
        }
        return result;
    }

	public static int findIndex(String source, String p)
	{
		Pattern pattern = Pattern.compile(p);
		Matcher find = pattern.matcher(source);
		int n = 0;
		while (find.find())
		{
			n++;
			if (n == 1)
			{
				break;
			}
		}
		return find.start();
	}

	public static Grade getGrade(String source)
	{
		Grade grade = new Grade();
		int l = findIndex(source, "\\[");
		int r = findIndex(source, "\\]");
		grade.examNum = source.substring(l + 1, r);
		grade.name = source.substring(r + 2, r + 4);
		grade.eng = source.substring(findIndex(source, "外语") + 31, findIndex(source, "外语") + 32);
		grade.chinese = source.substring(findIndex(source, "语文") + 31, findIndex(source, "语文") + 32);
		grade.math = source.substring(findIndex(source, "数学") + 31, findIndex(source, "数学") + 32);
		grade.phy = source.substring(findIndex(source, "物理") + 31, findIndex(source, "物理") + 32);
		grade.chem = source.substring(findIndex(source, "化学") + 31, findIndex(source, "化学") + 32);
		grade.bio = source.substring(findIndex(source, "生物") + 31, findIndex(source, "生物") + 32);
		grade.pol = source.substring(findIndex(source, "政治") + 31, findIndex(source, "政治") + 32);
		grade.his = source.substring(findIndex(source, "历史") + 31, findIndex(source, "历史") + 32);
		grade.geo = source.substring(findIndex(source, "地理") + 31, findIndex(source, "地理") + 32);
		return grade;
	}

	public static void printGrade(Grade grade)
	{
		System.out.println("准考证号:" + grade.examNum);
		System.out.println("姓名:" + grade.name);
		System.out.println("语文:" + grade.chinese);
		System.out.println("数学:" + grade.math);
		System.out.println("英语:" + grade.eng);
		System.out.println("物理:" + grade.phy);
		System.out.println("化学:" + grade.chem);
		System.out.println("生物:" + grade.bio);
		System.out.println("政治:" + grade.pol);
		System.out.println("历史:" + grade.his);
		System.out.println("地理:" + grade.geo);
	}
}
