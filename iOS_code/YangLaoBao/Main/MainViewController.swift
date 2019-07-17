//
//  MainViewController.swift
//  YangLaoBao
//
//  Created by 魏汉杰 on 2019/7/8.
//  Copyright © 2019年 魏汉杰. All rights reserved.
//

import UIKit

class MainViewController: UIViewController {
    
    @IBOutlet weak var pic1: UIImageView!
    @IBOutlet weak var pic2: UIImageView!
    @IBOutlet weak var word: UILabel!
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        if (UserDefaults().string(forKey: "isLogged")! == "yes") {
            word.text = "心电图"
            pic1.image = UIImage(named: "1")
            pic2.image = UIImage(named: "2")
        } else {
            word.text = "请先点击左上角登录哦"
            pic1.image = UIImage(named: "pic1")
            pic2.image = UIImage(named: "")
        }
        self.view.addSubview(self.pic1)
        self.view.addSubview(self.pic2)
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationItem.title = "我的体征"
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}
