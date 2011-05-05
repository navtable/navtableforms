# Name of abeille form
formname          = "ejemplo1form.xml"

# How the packages are going to be named
pkgnameform       = "es.udc.cartolab.gvsig.navtableformsexample"
pkgnamevalidation = "es.udc.cartolab.gvsig.navtableformsexample.validation"

baseClassName     = "Example1"
baseObjectName    = "example1"

formtitle         = "Example 1" # title of the java form
layerinxml        = "example1"  # the alias of the layer as defined in the XML
enumlayers        = ["example1"] # array with layers this form will work on. The first is the main one.
varsnonwidget     = ['']

pks               = "code" #i.e.: "id_conf, id_alt"

# If you want to inherit from sqlite form or not
sqlite            = False
sqlitepath        = ''

# Usually, you should not need to touch the following vars
# --------------------------------------------------------

# Templates
templateform      = "./tmpl/FormJava.tmpl"
templatemodel     = "./tmpl/Model.tmpl"
templatevalidator = "./tmpl/ModelValidator.tmpl"
templatebinding   = "./tmpl/ModelBinding.tmpl"

classform         = baseClassName  + "Form"
classmodel        = baseClassName  + "Model"
objectmodel       = baseObjectName + "Model"
classbinding      = baseClassName  + "Binding"
objectbinding     = baseObjectName + "Binding"
classvalidator    = baseClassName  + "Validator"
