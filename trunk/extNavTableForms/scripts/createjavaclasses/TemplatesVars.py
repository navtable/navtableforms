templateform      = "./tmpl/FormJava.tmpl"
templatemodel     = "./tmpl/Model.tmpl"
templatevalidator = "./tmpl/ModelValidator.tmpl"
templatebinding   = "./tmpl/ModelBinding.tmpl"

formname          = "centro_salud.xml"
sqlite            = False
sqlitepath        = ''

pkgnameform       = "es.udc.cartolab.gvsig.pmf.forms"
pkgnamevalidation = "es.udc.cartolab.gvsig.pmf.forms.validation"

baseClassName     = "CentroSalud"
baseObjectName    = "centroSalud"
classform         = baseClassName + "Form"
classmodel        = baseClassName + "Model"
objectmodel       = baseObjectName + "Model"
classbinding      = baseClassName + "Binding"
objectbinding     = baseObjectName + "Binding"
classvalidator    = baseClassName + "Validator"

formtitle         = "Centros de Salud" # title of the java form
layerinxml        = "centros_salud"
enumlayers        = ["centros_salud"] # array with layers this form will work on. The first is the main one.
varsnonwidget     = ['']

pks               = "cod_csalud" #i.e.: "id_conf, id_alt"
