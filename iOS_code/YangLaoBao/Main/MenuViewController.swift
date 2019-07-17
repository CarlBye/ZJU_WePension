//
//  MenuViewController.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/8.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit

class MenuViewController: UIViewController {
    
    @IBOutlet var userButton: UIButton!

    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        let isLogged: String = UserDefaults().string(forKey: "isLogged")!
        if (isLogged == "yes") {
            userButton.setTitle("\(UserDefaults().string(forKey: "userName")!)", for: .normal)
        }
        if (isLogged == "no"){
            userButton.setTitle("点击登录", for: .normal)
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @IBAction func myMessage() {
        if (UserDefaults().string(forKey: "isLogged")! == "yes") {
            let vc = ViewController()
            self.present(vc, animated: true, completion: nil)
        }
        return
    }
    
    @IBAction func gotoLoginOrSetting() {
        let isLogged = UserDefaults().string(forKey: "isLogged")!
        if (isLogged == "yes") {
            let vc = self.storyboard?.instantiateViewController(withIdentifier: String(describing: "setting"))
                as! SettingViewController
            self.navigationController?.pushViewController(vc, animated: true)
        }
        if (isLogged == "no") {
            let vc = self.storyboard?.instantiateViewController(withIdentifier: String(describing: "login"))
                as! LoginViewController
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }

}
