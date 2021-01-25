#API return fashion item 

import requests
from bs4 import BeautifulSoup
import time
import json
import toolz
from selenium import webdriver

class product:  
    def __init__(self, product_name, img_url, p_price, p_url ):  
        self.product_name = product_name  
        self.img_url = img_url
        self.p_price = p_price
        self.p_url = p_url
        
def domodi(productlist, input_name):

    url = "https://domodi.pl/wyszukaj?search=" + input_name + "&Sort=score"
    
    page = requests.get(url)
    soup = BeautifulSoup(page.content, 'html.parser')
    elements = soup.find_all("div", {"class": "dm-card__wrap"})[:20]
    for a in elements:
        try:
            p_name = a.find("h4", {"class":"_dbl dm-1of1"}).a.text.strip().split("-",1)[0]
            p_price = a.find("a", {"class":"dm-price-light dm-card__price"}).span.text.replace("zł","")
            p_img_url = a.find("div", {"class":"dm-card__hold"}).a.img["src"]
            if(p_img_url == "/content/img/blank.gif"):
                p_img_url = a.find("div", {"class":"dm-card__hold"}).a.img["data-original"]
            p_url = a.find("div", {"class":"dm-card__hold"}).a["href"]
            
        except:
            return False
            
        productlist.append(product(p_name, p_img_url, p_price, "https://domodi.pl" + p_url))       
    
def born2be(productlist, input_name):
     
    url = "https://born2be.pl/kobiety?q=" + input_name
    
    browser = webdriver.Chrome()
    browser.get(url)
    
    time.sleep(2)
    elements = browser.find_elements_by_class_name("product.product-click-event.dl-product-hover.gtm-product-click")[:20]
    
    for a in elements: 
        
        try:
            p_name = a.find_element_by_class_name("product__name").text
            
            p_price = a.find_element_by_xpath('//*[@class="product__price"]/div').text[:-3]
        
            p_img_url = a.find_element_by_class_name("main-img-product").get_attribute("src")
   
            p_url = a.find_element_by_class_name("main-img.dl-product-click").get_attribute("href")
            
           
        except:
            browser.close()
            return False
            
        productlist.append(product(p_name, "https://born2be.pl" + p_img_url, p_price, "https://born2be.pl" + p_url))     
    browser.close()
    
    
def main(input_name):
    productlist = []
    domodi(productlist, input_name)
    born2be(productlist, input_name)
    
    productlist = toolz.unique(productlist, key=lambda x: x.product_name)

    json_string = json.dumps([ob.__dict__ for ob in productlist], ensure_ascii=False).encode('utf8')
    return json_string
 



