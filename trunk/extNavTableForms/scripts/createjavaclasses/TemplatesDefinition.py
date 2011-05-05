from Cheetah.Template import Template

class JavaCodeTemplateVars(Template):
      # vars to fill out before calling cheetah template to generate code
      packagename    = ""
      pkgnamevalidation = ""
      classform      = ""
      enumlayers     = [] # array with layers this form will work on. The first is the main one.
      formname       = ""
      formtitle      = ""
      sqlite         = None
      classmodel     = ""
      objectmodel    = ""
      classbinding   = ""
      objectbinding  = ""
      classvalidator = ""

      nro_widgets    = None
      hastables      = None # boolean, indicates if a form has tables
      hasbuttons     = None # boolean, indicates if a form has buttons

      widget         = [] # nameofcomponentCB
      widget_name    = [] # nameofcomponent
      widget_type    = [] # TFT, CB, ...

      id_name        = [] # ID_NAMEOFCOMPONENT
      id_string      = [] # nameofcomponent.TFT.INT
      idx_name       = [] # nameofcomponent_idx

      table_names    = {} # keys are widget's name
      table_pks      = {} # keys are widget's name
      table_dbfs     = {} # keys are widget's name
      pks            = []

      tablewidgets   = [] # names of tables
      buttonwidgets  = [] # names of buttons


class ModelTemplate(Template):
      """Structure to store all variables needed for the validation template file."""
      packagename   = ""
      classmodel    = ""
      varsstring    = [None]
      varsboolean   = [None]
      varsint       = [None]
      varsreal      = [None]
      varsnonwidget = ""
      enumlayers    = ""


class ModelValidatorTemplate(Template):
      """Structure to store all variables needed for the validation template file."""
      packagename    = "" #i.e.: es.udc.cartolab.gvsig.validation
      classvalidator = "" #i.e.: FonsaguaPlanteamientoValidator
      classmodel     = "" #i.e.: FonsaguaPlanteamientoModel
      objectmodel    = "" #i.e.: fonsaguaPlanteamientoModel
      varsint        = [None]
      varsreal       = [None]


class BindingTemplate(Template):
      """Structure to store all variables needed for the binding template file."""
      packagename    = ""
      classbinding   = ""
      classmodel     = ""
      objectmodel    = ""
      classvalidator = ""
