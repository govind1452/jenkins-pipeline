import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.*;


@WebServlet("/Calculator")
public class Calculator extends HttpServlet {
    public int hitcount;
    public String exp;
    public void init()
    {
        hitcount=0;
        exp="0";
    }
    public void delete(){
        if(exp.length()>=1)
        exp=exp.substring(0,exp.length()-1);

    }
    public void calculate()
    {
        Stack <Double> s=new Stack<Double>();
        double num=0.0;
        int flag=0,n=10;
        char sign='+';
        for(int i=0;i<=exp.length();i++)
        {
            if(i<exp.length()&&exp.charAt(i)=='.')
            {
                flag=1;
            }
            else if(i<exp.length()&&exp.charAt(i)>='0'&&exp.charAt(i)<='9')
            {   if(flag==0)
                num=num*10+Double.valueOf(exp.substring(i,i+1));
                if(flag==1)
                {
                   num=num+Double.valueOf(exp.substring(i,i+1))/n;
                   n=n*10;
                }
            }
            else{
                if(sign=='+')
                {
                    s.push(num);
                    num=0;
                }else if(sign=='-')
                {
                    num=(-1)*num;
                    s.push(num);
                    num=0;
                }else if(sign=='/')
                {
                    double t=s.peek();
                    s.pop();
                    t=t/num;
                    s.push(t);
                    num=0;
                }
                else if(sign=='*')
                {
                    double t=s.peek();
                    s.pop();
                    t=t*num;
                    s.push(t);
                    num=0;
                }
                if(i!=exp.length())
                {
                    sign=exp.charAt(i);
                    n=10;
                    flag=0;
                }
            }
        }
        double result=0;
        while(!s.empty()){
            result=result+s.peek();
            s.pop();
        }
        exp=String.valueOf(result);
    }
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String s1=request.getParameter("val");
        System.out.println(s1);
        if(exp.length()>=1&&(exp.charAt(exp.length()-1)<'0'||exp.charAt(exp.length()-1)>'9')&&(s1.charAt(0)<'0'||s1.charAt(0)>'9'))
        {   if(s1.charAt(0)=='.')
            exp=exp+s1;
            else
            exp=exp;
        }
        else{
            if (hitcount == 1) {
                exp = s1;
            } else if (s1.equals("reset")) {
                init();
            } else if (s1.equals("delete")) {
                delete();
            } else if (s1.equals("=")) {
                calculate();
            } else {
                exp = exp + s1;
            }
        }
        RequestDispatcher rd=request.getRequestDispatcher("/index.jsp");


        //HttpSession session=request.getSession();
        request.setAttribute("exp-result",exp);
        rd.forward(request,response);

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
           hitcount++;

          service(request,response);
        //response.getWriter().println(exp);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    hitcount++;
        service(request,response);
    }
}
