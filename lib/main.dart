import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(app());
}

class app extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: app_page(),
    );
  }
}

class app_page extends StatelessWidget {
  TextEditingController fil = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        backgroundColor: Colors.white,
        title: Text(
          "Home Page",
          style: TextStyle(color: Colors.black),
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.all(9.0),
        child: Center(
          child: Container(
            padding: EdgeInsets.all(20),
            // alignment: Alignment.center,
            width: 500,
            height: 500,
            decoration: BoxDecoration(
                color: Colors.white, borderRadius: BorderRadius.circular(20)),
            child:
                Column(mainAxisAlignment: MainAxisAlignment.center, children: [
              SizedBox(
                height: 30,
              ),
              Text(
                'Welcome To Login Page',
                style: TextStyle(
                  color: Colors.blue,
                  fontSize: 40,
                  fontWeight: FontWeight.bold,
                ),
              ),
              SizedBox(
                height: 30,
              ),
              TextField(
                controller: fil,
                cursorColor: Colors.green,
                decoration: InputDecoration(
                    hintText: "Enter Your Name plz",
                    fillColor: Colors.blueGrey,
                    labelText: "Whats Your Name",
                    border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(30),
                        borderSide:
                            BorderSide(color: Colors.yellow, width: 2))),
              ),
              SizedBox(
                height: 30,
              ),
              TextField(
                decoration: InputDecoration(
                  hintText: "Enter Your Password plz",
                  labelText: "Whats Your Password",
                  border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(30),
                      borderSide: BorderSide(color: Colors.yellow, width: 2)),
                ),
              ),
              SizedBox(
                height: 30,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  ElevatedButton(
                      onPressed: () {
                        print('Login');
                      },
                      child: Text("Login")),
                  SizedBox(
                    width: 20,
                  ),
                  ElevatedButton(
                      onPressed: () {
                        print('Cancle');
                      },
                      child: Text("Cancle"))
                ],
              ),
              SizedBox(
                height: 20,
              ),
              TextButton(
                onPressed: () {
                  print("Forgot Password");
                },
                child: Text("Forgot Password"),
              ),
              Text.rich(
                TextSpan(
                    text: "Don't have an account?",
                    style: TextStyle(color: Colors.black),
                    children: [
                      TextSpan(text: " "),
                      TextSpan(
                          text: "Sign Up",
                          style: TextStyle(color: Colors.blue),
                          recognizer: TapGestureRecognizer()
                            ..onTap = () {
                              print("Sign Up");
                            }),
                    ]),
              ),
            ]),
          ),
        ),
      ),
    );
  }
}
