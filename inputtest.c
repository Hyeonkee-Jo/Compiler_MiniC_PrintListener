int max=180; void main(int a) {
int number[3];
    int num; int temp;int value;
    temp=10; num=1;
number[0] =1;
number[1]=2;
number[2]=5;
    while(num< 10){
        ++num;
    }
    num = num -9;
    if(num == 10) {
        value=number[1]/number[2];
    }
    else if(num <= 15){
        value =2;
     }
     else if(num != 1) value =5;
    else
        value=0;

	return 0;
} void calculate(int first,int second,int third){
    int result;
    result= first * second / third;
    printf(result);
}