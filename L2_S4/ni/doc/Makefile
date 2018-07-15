all: rapport.pdf

rapport.pdf: rapport.tex
	latexmk -pdf rapport.tex
	@latexmk -c rapport.tex

clean:
	@latexmk -c rapport.tex
	@rm -f rapport.pdf
