**Input**

请输入总的请求数  
4000  
请输入并发数  
1000  
tsv文件存放文件夹路径,文件名本程序自动命名  
K:/lightfish/  
测试的URL,格式:[http://]hostname[:port]  
http://localhost:8080/   

**Output**  

ab -n 4000 -c 1000 -g K:/lightfish/1.tsv http:/localhost:8080/1  
ab -n 4000 -c 1000 -g K:/lightfish/2.tsv http:/localhost:8080/2  
ab -n 4000 -c 1000 -g K:/lightfish/4.tsv http:/localhost:8080/4  
ab -n 4000 -c 1000 -g K:/lightfish/8.tsv http:/localhost:8080/8  
ab -n 4000 -c 1000 -g K:/lightfish/16.tsv http:/localhost:8080/16  
ab -n 4000 -c 1000 -g K:/lightfish/32.tsv http:/localhost:8080/32  
ab -n 4000 -c 1000 -g K:/lightfish/64.tsv http:/localhost:8080/64    

  

\#output as png image  
set terminal png size 1000,560  
\#save file to "domain.png"  
set output "biz.png"  
\#graph title  
set title "Biz Performance"  
set key invert reverse Left outside  
\#nicer aspect ratio for image size  
\#set size 1,0.7  
\# y-axis grid  
set grid y  
\#x-axis label  
set xlabel "requests"  
\#y-axis label  
set ylabel "response time (ms)"  
\#plot data from "biz.dat" using column 9 with smooth sbezier lines  
\#and title of "Biz Performance" for the given data  
plot "K:/lightfish//1.tsv" using 9 smooth sbezier with lines title "1k",\  
"K:/lightfish//2.tsv" using 9 smooth sbezier with lines title "2k",\  
"K:/lightfish//4.tsv" using 9 smooth sbezier with lines title "4k",\  
"K:/lightfish//8.tsv" using 9 smooth sbezier with lines title "8k",\  
"K:/lightfish//16.tsv" using 9 smooth sbezier with lines title "16k",\  
"K:/lightfish//32.tsv" using 9 smooth sbezier with lines title "32k",\  
"K:/lightfish//64.tsv" using 9 smooth sbezier with lines title "64k"  

