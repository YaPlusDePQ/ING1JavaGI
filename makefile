# Compiler
JC = javac

# Dossiers source et de destination
SRCDIR = src
BINDIR = bin

# Flags de compilation
PATH_TO_FX := "C:/Program Files/Java/javafx-sdk-22.0.1/lib"
JCFLAGS := -d $(BINDIR)/ -cp $(SRCDIR)/ --module-path $(PATH_TO_FX) --add-modules javafx.controls,javafx.fxml

# Liste des fichiers source
SOURCES := $(wildcard $(SRCDIR)/*.java)

# Liste des fichiers class
CLASSES := $(SOURCES:$(SRCDIR)/%.java=$(BINDIR)/%.class)

# Commande pour construire les fichiers .class
$(BINDIR)/%.class: $(SRCDIR)/%.java
	$(JC) $(JCFLAGS) $<

# Règle par défaut pour construire tous les fichiers .class
all: $(CLASSES)

# Nettoyer les fichiers générés
clean:
	$(RM) $(BINDIR)/*.class