import 'dart:io';

void main(){
/*  //var variable atir data poriborton shil last je velu deya hbe oyta jma thakbe ,,,
  var name  ='nahid hasan';
  name ='nahid';
  print(name);

  //fainal variable er kaj hlo akbar jkhon er valu set kra hbe  tar pore ar kono velu change kra jabe na,,
  final roll = 235;
  ///roll = 458;
  print(roll);

  const title='welcome to my application';
  //title ='welcome';
  print(title);


  // dynamic ati var er mto kaj kore,,
  dynamic no=01312072689;
  no=123456789;
  print(no);


  print('___________________________________________________________________________');
  print('');

//   match operation
//   variable declariation
var num_1= 546;
  var num_2= 598;
print('The Number $num_1 and $num_2 is sumed up to ');*/
print("enter The Number 1 :");

int numbar_1 = int.parse(stdin.readLineSync()!);

print("enter The Number 2 :");
int numbar_2 = int.parse(stdin.readLineSync()!);

int addition = numbar_1 + numbar_2;
int substraction = numbar_1 - numbar_2;
int multiplication = numbar_1 * numbar_2;
int division = numbar_1 ~/ numbar_2;
int reminder = numbar_1 % numbar_2;
print("addition , substraction , multiplication , division , reminder is $addition , $substraction , $multiplication , $division , $reminder");

print("nahid hasan");
}