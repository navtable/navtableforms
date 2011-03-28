templateform      = "./tmpl/FormJava.tmpl"
templatemodel     = "./tmpl/Model.tmpl"
templatevalidator = "./tmpl/ModelValidator.tmpl"
templatebinding   = "./tmpl/ModelBinding.tmpl"
# name file of the abeille form
formname          = "ejemplo1form.xml"

pkgnameform       = "es.udc.cartolab.gvsig.navtableformsexample"
pkgnamevalidation = "es.udc.cartolab.gvsig.navtableformsexample.validation"

baseClassName     = "Example1"
baseObjectName    = "example1"


formtitle         = "Example 1" # title of the java form
layerinxml        = "example1"
enumlayers        = ["example1"] # array with layers this form will work on. The first is the main one.
varsnonwidget     = ['']

pks               = "code" #i.e.: "id_conf, id_alt"

# Yo should not need to touch the following vars



sqlite            = False
sqlitepath        = ''

classform         = baseClassName + "Form"
classmodel        = baseClassName + "Model"
objectmodel       = baseObjectName + "Model"
classbinding      = baseClassName + "Binding"
objectbinding     = baseObjectName + "Binding"
classvalidator    = baseClassName + "Validator"

