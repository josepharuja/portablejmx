<%
StringBuffer sb = new StringBuffer();			
int ensureCapacity = 2097152 ;
sb.ensureCapacity(ensureCapacity);			
java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(request.getInputStream()));
char buf[] = new char[131072];
int n;
int offset = 0;

while ((n = br.read(buf,offset,131072)) != -1) 
{
	sb.append(buf,0,n);	
}
System.out.println(sb.toString());
%>
<%
response.setStatus(202);
%>