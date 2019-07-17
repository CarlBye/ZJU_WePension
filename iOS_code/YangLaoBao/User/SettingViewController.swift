//
//  SettingViewController.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/13.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit
import Alamofire

class SettingViewController: UIViewController {
    
    @IBOutlet var updateUsernameTextField: UITextField!
    @IBOutlet var updateOriginalPasswordTextField: UITextField!
    @IBOutlet var updatePasswordTextField: UITextField!
    @IBOutlet var updateRePasswordTextField: UITextField!
    
    @IBAction func logout() {
        UserDefaults().set("no", forKey: "isLogged")
        UserDefaults().set("", forKey: "userID")
        let alert = UIAlertController(title: "您已退出登录", message: "", preferredStyle: .alert)
        let action = UIAlertAction(title: "确定", style: .default, handler: {
            action in
            self.navigationController?.popViewController(animated: true)
        })
        alert.addAction(action)
        self.present(alert, animated: true, completion: nil)
    }
    
    @IBAction func updateUsername() {
        let params = [
            "curId":"\(UserDefaults().string(forKey: "userID")!)",
            "newName":"\(updateUsernameTextField.text!)"
            ] as [String : Any]
        let headers: HTTPHeaders = ["Content-Type":"application/json", "Accept":"application/json"]
        Alamofire.request(url + "/api/user/update/name",
                          method: .post,
                          parameters: params,
                          encoding: JSONEncoding.default,
                          headers: headers).validate().responseJSON { response in
            if (response.result.isSuccess) {
                let resultDictionary = response.result.value as! NSDictionary
                if ((resultDictionary["IsSuccess"] as! Bool) == true) {
                    let alert = UIAlertController(title: "修改用户名成功", message: "", preferredStyle: .alert)
                    let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                    alert.addAction(action)
                    self.present(alert, animated: true, completion: nil)
                    UserDefaults().set(self.updateUsernameTextField.text!, forKey: "userName")
                } else {
                    let alert = UIAlertController(title: "修改用户名失败", message: "\(String(describing: resultDictionary["ErrorInfo"]!))", preferredStyle: .alert)
                    let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                    alert.addAction(action)
                    self.present(alert, animated: true, completion: nil)
                }
            } else {
                let alert = UIAlertController(title: "修改用户名失败", message: "网络错误", preferredStyle: .alert)
                let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                alert.addAction(action)
                self.present(alert, animated: true, completion: nil)
            }
        }
    }
    
    @IBAction func updatePassword() {
        if (updatePasswordTextField.text! != updateRePasswordTextField.text!) {
            let alert = UIAlertController(title: "", message: "两次新密码输入不一致", preferredStyle: .alert)
            let action = UIAlertAction(title: "确定", style: .default, handler: nil)
            alert.addAction(action)
            self.present(alert, animated: true, completion: nil)
        } else {
            let params = [
                "curId":"\(UserDefaults().string(forKey: "userID")!)",
                "oldPwd":"\(updateOriginalPasswordTextField.text!)",
                "newPwd":"\(updatePasswordTextField.text!)"
                ] as [String : Any]
            let headers: HTTPHeaders = ["Content-Type":"application/json", "Accept":"application/json"]
            Alamofire.request(url + "/api/user/update/pwd",
                              method: .post,
                              parameters: params,
                              encoding: JSONEncoding.default,
                              headers: headers).validate().responseJSON { response in
                if (response.result.isSuccess) {
                    let resultDictionary = response.result.value as! NSDictionary
                    if ((resultDictionary["IsSuccess"] as! Bool) == true) {
                        let alert = UIAlertController(title: "修改密码成功", message: "", preferredStyle: .alert)
                        let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                        alert.addAction(action)
                        self.present(alert, animated: true, completion: nil)
                    } else {
                        let alert = UIAlertController(title: "修改密码失败", message: "\(String(describing: resultDictionary["ErrorInfo"]!))", preferredStyle: .alert)
                        let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                        alert.addAction(action)
                        self.present(alert, animated: true, completion: nil)
                    }
                } else {
                    let alert = UIAlertController(title: "修改密码失败", message: "网络错误", preferredStyle: .alert)
                    let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                    alert.addAction(action)
                    self.present(alert, animated: true, completion: nil)
                }
            }
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        self.navigationItem.title = "设置"
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

}
