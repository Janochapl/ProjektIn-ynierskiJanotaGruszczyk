from flask import Flask
import itemList
import electronic
import house
import fashion
import literature

app = Flask(__name__)

@app.route('/')

def index():
    return "Chyba działa"

@app.route("/items-list/<categort_txt>/<input_txt>", methods = ['GET'])

def postList(categort_txt, input_txt):

    if(categort_txt == "1"):
        return itemList.electronic(input_txt)
    if(categort_txt == "2"):
        return itemList.wear(input_txt)
    if(categort_txt == "3"):
        return itemList.house(input_txt)
    else:
        return itemList.literature(input_txt)
    
    return False


@app.route("/items/<categort_txt>/<input_txt>", methods = ['GET'])

def postElecticItems(categort_txt, input_txt):

    if(categort_txt == "1"):
        return electronic.main(input_txt)
    if(categort_txt == "2"):
        return fashion.main(input_txt)
    if(categort_txt == "3"):
        return house.main(input_txt)
    else:
        return literature.main(input_txt)
    return False
 

if __name__ == "__main__":
     app.run(host='0.0.0.0', port=80, debug=False)
    