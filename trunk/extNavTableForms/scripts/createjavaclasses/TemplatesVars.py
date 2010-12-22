templateform      = "./tmpl/FormJava.tmpl"
templatemodel     = "./tmpl/Model.tmpl"
templatevalidator = "./tmpl/ModelValidator.tmpl"
templatebinding   = "./tmpl/ModelBinding.tmpl"

formname          = "RegistroMaterial.xml"
sqlitepath        = '/home/amaneiro/cartolab/arqueologiaponte/Data/_DB/arqueoPonteBD.sqlite3'

pkgnameform       = "es.udc.cartolab.gvsig.arqueoponte.forms"
pkgnamevalidation = "es.udc.cartolab.gvsig.arqueoponte.validation"

classform         = "MaterialRegistrationForm"
classmodel        = "MaterialRegistrationModel"
objectmodel       = "materialRegistrationModel"
classbinding      = "MaterialRegistrationBinding"
objectbinding     = "materialRegistrationBinding"
classvalidator    = "MaterialRegistrationValidator"

formtitle         = "Registro de Material" # title of the java form
layerinxml        = "Registro_Material"
enumlayers        = ["Registro_Material"] # array with layers this form will work on. The first is the main one.
varsnonwidget     = ['']

pks               = "codPie" #i.e.: "id_conf, id_alt"
