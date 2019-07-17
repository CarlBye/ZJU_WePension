//
//  AddShoppingButtonViewController.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/13.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit
import Alamofire

class AddShoppingButtonViewController: UIViewController {
    
    @IBOutlet var bindingButton: UILabel!
    @IBOutlet var bindingCommodity: UILabel!
    @IBOutlet var num: UITextField!
    @IBOutlet var stepper: UIStepper!
    @IBOutlet var address: UITextField!
    @IBOutlet var phone: UITextField!
    @IBOutlet var person: UITextField!
    
    @IBAction func submit() {
        let params = [
            "curId":"\(UserDefaults().string(forKey: "userID")!)",
            "buttonId":"\(UserDefaults().string(forKey: "IDOfSelectedButton")!)",
            "comId":"\(UserDefaults().string(forKey: "keyOfSelectedCommodity")!)",
            "num":"\(num.text!)",
            "comDeliveryAddress":"\(address.text!)",
            "comDeliveryPhone":"\(phone.text!)",
            "comDeliveryName":"\(person.text!)"
            ] as [String : Any]
        let headers: HTTPHeaders = ["Content-Type":"application/json", "Accept":"application/json"]
        Alamofire.request(url + "/api/button/bind/commodity",
                        method: .post,
                        parameters: params,
                        encoding: JSONEncoding.default,
                        headers: headers).validate().responseJSON { response in
            if (response.result.isSuccess) {
                let result = response.result.value
                let resultDictionary = result as! NSDictionary
                if ((resultDictionary["IsSuccess"] as! Bool) == true) {
                    let alert = UIAlertController(title: "绑定成功", message: "", preferredStyle: .alert)
                    let action = UIAlertAction(title: "确定", style: .default, handler: {
                        action in
                        self.navigationController?.popViewController(animated: true)
                    })
                    alert.addAction(action)
                    self.present(alert, animated: true, completion: nil)
                } else {
                    let alert = UIAlertController(title: "绑定失败", message: "\(String(describing: resultDictionary["ErrorInfo"]!))", preferredStyle: .alert)
                    let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                    alert.addAction(action)
                    self.present(alert, animated: true, completion: nil)
                }
            } else {
                let alert = UIAlertController(title: "绑定失败", message: "网络错误", preferredStyle: .alert)
                let action = UIAlertAction(title: "确定", style: .default, handler: nil)
                alert.addAction(action)
                self.present(alert, animated: true, completion: nil)
            }
        }
    }
    
    @objc func stepperChanged() {
        let numText: String = String(Int(stepper.value))
        num.text = numText
    }
    
    override func viewWillAppear(_ animated: Bool) {
        bindingButton.text = UserDefaults().string(forKey: "nameOfSelectedButton")!
        bindingCommodity.text = UserDefaults().string(forKey: "nameOfSelectedCommodity")!
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationItem.title = "购物按钮"
        
        UserDefaults().set("尚未选择", forKey: "nameOfSelectedButton")
        UserDefaults().set("", forKey: "IDOfSelectedButton")
        UserDefaults().set("尚未绑定", forKey: "nameOfSelectedCommodity")
        
        //设置最大和最小值
        stepper.maximumValue = 10
        stepper.minimumValue = 1
        //当前值
        stepper.value = 1
        //每次变化的单位
        stepper.stepValue = 1
        //按住的时候连续变化
        stepper.isContinuous = true
        //是否循环 当增长到最大值的时候再从新开始
        stepper.wraps = true
        //改变事件添加
        stepper.addTarget(self, action: #selector(stepperChanged), for: .valueChanged)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

}
