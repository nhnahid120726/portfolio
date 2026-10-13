import 'dart:io';

void main() {
  //  ব্যবহারকারীর (User) কাছ থেকে তার নাম এবং বয়স ইনপুট নাও
  /* stdout.write("Enter Your name : ");
  String? name = stdin.readLineSync();
  stdout.write("Enter Your Age : ");
  int age = int.parse(stdin.readLineSync()!);
  
  print("hello, My Name is $name, and age $age years old");
  */

  // ইউজারের কাছ থেকে একটি পূর্ণসংখ্যা (Integer) ইনপুট নাও। সংখ্যাটি জোড় (Even) নাকি বিজোড় (Odd) তা পরীক্ষা করে কনসোলে প্রিন্ট করো।

/*  stdout.write("Enter Your Integer Number : ");
  int number = int.parse(stdin.readLineSync()!);

  if (number > 0){
    if(number % 2 == 0 ){
      print("This number is even");
    }else{
      print("This number is odd");
    }
  }else{
    print("Enter a valid and positiv number");
  }*/

//ইউজারের কাছ থেকে একটি পরীক্ষার নম্বর (Marks: 0 থেকে 100-এর মধ্যে পূর্ণসংখ্যা) ইনপুট নাও। তারপর নিচের শর্ত অনুযায়ী তার গ্রেড প্রিন্ট করো:

  /* stdout.write("Enter Your Marks : ");
  int marks = int.parse(stdin.readLineSync()!);

  if (marks >= 0 && marks <= 100) {
    if (marks >= 80) {
      print("Your Grade is A+");
    } else if (marks >= 70) {
      print("Your Grade is A");
    } else if (marks >= 60) {
      print("Your Grade is A-");
    } else if (marks >= 50) {
      print("Your Grade is B");
    } else if (marks >= 40) {
      print("Your Grade is C");
    } else if (marks >= 33) {
      print("Your Grade is D");
    } else {
      print("Your Grade is F");
    }
  } else {
    print("Enter a valid Number between 0 to 100");
  }
*/

  //ইউজারের কাছ থেকে একটি সাল (Year) ইনপুট নাও (যেমন 2024, 2000, 1900)। বছরটি Leap Year (অধিবর্ষ) নাকি সাধারণ বছর, তা নির্ণয় করে প্রিন্ট করো।

/*
  stdout.write("Enter Years : ");
  int lip = int.parse(stdin.readLineSync()!);

  if (lip % 4 == 0 && lip % 100 != 0 || lip % 400 == 0){
    print("Leap Year");
  }else{
    print("Not Leap Year");
  }*/

  /*stdout.write("enter the first number");
  int number = int.parse(stdin.readLineSync()!);
  stdout.write("enter the second number");
  int number2 = int.parse(stdin.readLineSync()!);
  stdout.write("enter the third number");
  int number3 = int.parse(stdin.readLineSync()!);

  if (number > number2 && number > number3) {
    print("Number $number is leargest number 1");
  } else if (number2 > number && number2 > number3) {
    print("Number $number2 is leargest number 2");
  } else if (number3 > number && number3 > number2) {
    print("Number $number3 is leargest number 3");
  } else if (number == number2 && number == number3) {
    print("All numbers are equal");
  } else if (number == number2) {
    print("Number a and number b are equal");
  } else if (number == number3) {
    print("Number a and number c are equal");
  } else if (number2 == number3) {
    print("Number b and number c are equal");
  }*/

  //ইউজারের কাছ থেকে পর্যায়ক্রমে ৩টি ইনপুট নাও:
  //
  // প্রথম সংখ্যা (double হিসেবে)
  //
  // একটি অপারেটর সংকেত (+, -, *, /)
  //
  // দ্বিতীয় সংখ্যা (double হিসেবে)
/*
stdout.write("Enter First number : ");
double num_1 = double.parse(stdin.readLineSync()!);
stdout.write("Enter Second number : ");
double num_2 = double.parse(stdin.readLineSync()!);
stdout.write("Enter Operation : ");
String ope = stdin.readLineSync()!;

  double output =0 ;

 if (ope == "+"){
   output=num_1+num_2;
 }else if (ope == "-"){
   output=num_1-num_2;
 }else if (ope == "*"){
   output=num_1*num_2;
 }else if (ope == "/"){
   if (num_2 ==0){
     print("Cannot divide by zero");
   }else{
     output=num_1/num_2;
   }
 }else if (ope == "%"){
   output=num_1%num_2;
 }
 print("Result : $output");

*/


}
