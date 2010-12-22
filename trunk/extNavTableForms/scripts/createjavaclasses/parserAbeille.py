#!`which python`

from BeautifulSoup import BeautifulStoneSoup
import re

class ParserAbeille():

    # CB = ComboBox
    # TF = TextField
    # FTF = FormatedTextField
    # TA = TextArea
    # TB = Table
    # BT = Button
    # RB = RadioButton
    # CHB = CheckBox

    def __init__(self, file):

        f = open(file, 'rb')
        xml = f.read()
        self.soup = BeautifulStoneSoup(xml)
        f.close()

    def getComboBoxes(self):
        """Get all CBs from an Abeille file and return a list with them."""
        cbs = self.soup.findAll(text=re.compile("\.CB"))
        return cbs

    def getTextFields(self):
        """Get all TFs from an Abeille file and return a list with them."""
        tfs = self.soup.findAll(text=re.compile("\.TF"))
        return tfs

    def getFormattedTextFields(self):
        """Get all FTFs from an Abeille file and return a list with them."""
        ftfs = self.soup.findAll(text=re.compile("\.FTF"))
        return ftfs

    def getTextAreas(self):
        """Get all TAs from an Abeille file and return a list with them."""
        tas = self.soup.findAll(text=re.compile("\.TA"))
        return tas

    def getTables(self):
        """Get all TBs from an Abeille file and return a list with them."""
        tbs = self.soup.findAll(text=re.compile("\.TB"))
        return tbs

    def hasTables(self):
        if (len(self.getTables()) > 0):
            bool = True
        else:
            bool = False
        return bool

    def hasButtons(self):
        if (len(self.getButtons()) > 0):
            bool = True
        else:
            bool = False
        return bool

    def getButtons(self):
        """Get all BTs from an Abeille file and return a list with them."""
        bts = self.soup.findAll(text=re.compile("\.BT"))
        return bts

    def getRadioButtons(self):
        """Get all RBs from an Abeille file and return a list with them."""
        rbs = self.soup.findAll(text=re.compile("\.RB"))
        return rbs

    def getCheckBoxes(self):
        """Get all CHBs from an Abeille file and return a list with them."""
        chbs = self.soup.findAll(text=re.compile("\.CHB"))
        return chbs

#    def getAllWidgets(self):
#        widgets = []
#        widgets.append(self.getWidgetsWithoutChecking())
#        widgets.append(self.getWidgetsWithChecking())
#        return widgets

    def getWidgetsWithContent(self):
        """Retrieve all components with content to be store in the mode: TF, FTF, CB, CHB & TA"""

        widgets = []
        for i in self.getTextFields():
            widgets.append(i)

        for i in self.getFormattedTextFields():
            widgets.append(i)

        for i in self.getComboBoxes():
            widgets.append(i)

        for i in self.getCheckBoxes():
            widgets.append(i)

        for i in self.getTextAreas():
            widgets.append(i)

        return widgets

    def getWidgetsWithChecking(self):
        """Get all components with checking -FTFs- from an Abeille file and return a list with them."""

        widgets = []

        for i in self.getFormattedTextFields():
            widgets.append(i)

        return widgets

    def getWidgetsWithoutChecking(self):
        """Get all components with no checks -TFs, CBs, TAs, TBs, BTs, RBs, CHBs- from an Abeille file and return a list with them."""

        widgets = []

        for i in self.getComboBoxes():
            widgets.append(i)

        for i in self.getTextFields():
            widgets.append(i)

        for i in self.getTextAreas():
            widgets.append(i)

        for i in self.getTables():
            widgets.append(i)

        for i in self.getButtons():
            widgets.append(i)

        for i in self.getRadioButtons():
            widgets.append(i)

        for i in self.getCheckBoxes():
            widgets.append(i)

        return widgets


if __name__ == "__main__":

    import sys

    pa = ParserAbeille(sys.argv[1])

    w = pa.getComboBoxes()
    print len(w), "comboboxes: ", w

    w = pa.getTextFields()
    print len(w), "textfields: ", w

    w = pa.getFormattedTextFields()
    print len(w), "formattedtextfields: ", w

    w = pa.getTextAreas()
    print len(w), "textareas: ", w

    w = pa.getTables()
    print len(w), "tables: ", w

    w = pa.getButtons()
    print len(w), "buttons: ", w

    w = pa.getRadioButtons()
    print len(w), "radiobuttons: ", w

    w = pa.getCheckBoxes()
    print len(w), "checkboxes: ", w

    w = pa.getWidgetsWithChecking()
    print "\n", len(w), "widgets with checking: ", w

    w = pa.getWidgetsWithoutChecking()
    print "\n", len(w), "widgets without checking: ", w

#    w = pa.getAllWidgets()
#    print "\n", len(w), "widgets total: ", w
