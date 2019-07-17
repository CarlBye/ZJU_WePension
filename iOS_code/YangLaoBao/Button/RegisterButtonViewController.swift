//
//  RegisterButtonViewController.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/14.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit
import Alamofire

class RegisterButtonViewController: UIViewController {
    
    @IBOutlet weak var name: UITextField!
    @IBOutlet weak var ID: UITextField!
    
    @IBAction func registerButton() {
        let params = [
            "curId":"\(UserDefaults().string(forKey: "userID")!)",
            "buttonId":"\(ID.text!)",
            "buttonName":"\(name.text!)",
            ] as [String : Any]
        let headers: HTTPHeaders = ["Content-Type":"application/json", "Accept":"application/json"]
        Alamofire.request(url + "/api/button/bind",
                        method: .post,
                        parameters: params,
                        encoding: JSONEncoding.default,
                        headers: headers).validate().responseJSON { response in
            if (response.result.isSuccess) {
                let result = response.result.value
                let resultDictionary = result as! NSDictionary
                if ((resultDictionary["IsSuccess"] as! Bool) == true) {
                    let alert = UIAlertController(title: "按钮注册成功", message: "", preferredStyle: .alert)
                    let action = UIAlertAction(title: "确定", style: .default, handler: {
                        action in
                        self.navigationController?.popViewController(animated: true)
                    })
                    alert.addAction(action)
                    self.present(alert, animated: true, completion: nil)
                } else {
                    let alert = UIAlertController(title: "按钮注册失败", message: "\(String(describing: resultDictionary["ErrorInfo"]!))", preferredStyle: .alert)
                    let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                    alert.addAction(action)
                    self.present(alert, animated: true, completion: nil)
                }
            } else {
                let alert = UIAlertController(title: "按钮注册失败", message: "网络错误", preferredStyle: .alert)
                let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                alert.addAction(action)
                self.present(alert, animated: true, completion: nil)
            }
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationItem.title = "注册按钮"
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
