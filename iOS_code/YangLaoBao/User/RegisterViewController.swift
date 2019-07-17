//
//  RegisterViewController.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/11.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit
import Alamofire

class RegisterViewController: UIViewController {
    
    @IBOutlet var registerUsernameTextField: UITextField!
    @IBOutlet var registerPhoneTextField: UITextField!
    @IBOutlet var registerEmailTextField: UITextField!
    @IBOutlet var registerPasswordTextField: UITextField!
    @IBOutlet var registerRePasswordTextField: UITextField!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationItem.title = "注册"
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @IBAction func register() {
        let username = registerUsernameTextField.text
        let password = registerPasswordTextField.text
        let repassword = registerRePasswordTextField.text
        let phone = registerPhoneTextField.text
        let email = registerEmailTextField.text
        
        if ((username?.isEmpty)! || (password?.isEmpty)! || (repassword?.isEmpty)! || (phone?.isEmpty)! || (email?.isEmpty)! ) {
            let alert = UIAlertController(title: "注册失败", message: "输入项不允许为空", preferredStyle: .alert)
            let action = UIAlertAction(title: "确定", style: .default, handler: nil)
            alert.addAction(action)
            present(alert, animated: true, completion: nil)
        } else if (password != repassword) {
            let alert = UIAlertController(title: "注册失败", message: "两次密码输入不一致", preferredStyle: .alert)
            let action = UIAlertAction(title: "确定", style: .default, handler: nil)
            alert.addAction(action)
            present(alert, animated: true, completion: nil)
        } else {
            let params = [
                "regName":"\(username!)",
                "regPwd":"\(password!)",
                "regPhone":"\(phone!)",
                "regEmail":"\(email!)",
                "regDescription":"",
                "regFaceId":2
                ] as [String : Any]
            let headers: HTTPHeaders = ["Content-Type":"application/json", "Accept":"application/json"]
            Alamofire.request(url + "/api/user/register",
                      method: .post,
                      parameters: params,
                      encoding: JSONEncoding.default,
                      headers: headers).validate().responseJSON { response in
                if (response.result.isSuccess) {
                    let result = response.result.value
                    let resultDictionary = result as! NSDictionary
                    if ((resultDictionary["IsSuccess"] as! Bool) == true) {
                        let alert = UIAlertController(title: "注册成功", message: "", preferredStyle: .alert)
                        let action = UIAlertAction(title: "确定", style: .default, handler: {
                            action in
                            self.navigationController?.popViewController(animated: true)
                        })
                        alert.addAction(action)
                        self.present(alert, animated: true, completion: nil)
                    } else {
                        let alert = UIAlertController(title: "注册失败", message: "\(String(describing: resultDictionary["ErrorInfo"]!))", preferredStyle: .alert)
                        let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                        alert.addAction(action)
                        self.present(alert, animated: true, completion: nil)
                    }
                } else {
                    let alert = UIAlertController(title: "注册失败", message: "网络错误", preferredStyle: .alert)
                    let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                    alert.addAction(action)
                    self.present(alert, animated: true, completion: nil)
                }
            }
        }
        
        // TODO regex-match inputs
        
        return
    }
    
    @IBAction func jumpToLogIn() {
        self.navigationController?.popViewController(animated: true)
    }
    
    //键盘可隐藏
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        view.endEditing(true)
    }

}
