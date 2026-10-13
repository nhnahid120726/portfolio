import 'dart:io';

void main() {
  print("Enter The Valu in A :");
  int a = int.parse(stdin.readLineSync()!);
  print("Enter The Valu in B :");
  int b = int.parse(stdin.readLineSync()!);
  print("Enter The Valu in C :");
  int c = int.parse(stdin.readLineSync()!);
  // ১. তিনটি সংখ্যাই একই কিনা
  if (a == b && b == c) {
    print("সবগুলো সংখ্যা একই: $a");
  }
  // ২. দুইটি সংখ্যা একই কিনা
  else if (a == b) {
    print("a এবং b একই: $a");
  } else if (a == c) {
    print("a এবং c একই: $a");
  } else if (b == c) {
    print("b এবং c একই: $b");
  } else {
    print("কোনো সংখ্যা একই নয়");
  }

  // ৩. সবচেয়ে বড় সংখ্যা বের করা
  int max;

  if (a >= b && a >= c) {
    max = a;
  } else if (b >= a && b >= c) {
    max = b;
  } else {
    max = c;
  }

  print("সবচেয়ে বড় সংখ্যা: $max");
}
