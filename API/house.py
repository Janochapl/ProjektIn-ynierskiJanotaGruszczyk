#API return home item 

import json
import toolz
from selenium import webdriver


class product:  
    def __init__(self, product_name, img_url, p_price, p_url ):  
        self.product_name = product_name  
        self.img_url = img_url
        self.p_price = p_price
        self.p_url = p_url
        
def castorama(productlist, input_name):
     
    url = "https://www.castorama.pl/result/?q=" + input_name
    
    browser = webdriver.Chrome()
    browser.get(url)

    elements = browser.find_elements_by_class_name("sn-product--wrp")[:20]
    for a in elements:
        
        try:
            p_name = a.find_element_by_class_name("product-name").text
            
            p_price = a.find_element_by_class_name("price-value.price-box__value").text
            
            p_img_url = a.find_element_by_tag_name("img").get_attribute("src")
            
            p_url = a.find_element_by_tag_name("a").get_attribute("href")
            
            
        except:
            
            return False
            
        productlist.append(product(p_name, p_img_url, p_price, p_url))     
    browser.close()
    
    

def obi(productlist, input_name):
         
    url = "https://www.obi.pl/search/" + input_name
    
    browser = webdriver.Chrome()
    browser.get(url)


    elements = browser.find_elements_by_class_name("product-wrapper.wt_ignore")[:20]
    for a in elements:
        try:
            p_name = a.find_element_by_class_name("description").text
            
            p_price = a.find_element_by_class_name("price").text[:-2]
            
            p_img_url = a.find_element_by_tag_name("img").get_attribute("src")
            
            p_url = a.get_attribute("href")
            
        except:
            return False
            
        productlist.append(product(p_name, p_img_url, p_price, p_url))     
    browser.close()
        
        

    
    
def main(input_name):
    productlist = []
    castorama(productlist, input_name)
    obi(productlist, input_name)
    
    productlist = toolz.unique(productlist, key=lambda x: x.product_name)

    json_string = json.dumps([ob.__dict__ for ob in productlist], ensure_ascii=False).encode('utf8')
    return json_string
 



