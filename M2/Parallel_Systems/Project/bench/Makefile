PINTHREADS_SRC_DIR=tools/PinThreads

LIN_REG_SRC_DIR=benchmark/linear_regression
LIN_REG_INPUTS_URL=http://csl.stanford.edu/~christos/data/linear_regression.tar.gz
LIN_REG_INPUTS_ARCHIVE=tmp/lin_reg_input.tar.gz
LIN_REG_INPUTS=tmp/lin_reg_inputs
LIN_REG_INPUT_500MB=$(LIN_REG_INPUTS)/key_file_500MB.txt
LIN_REG_INPUT_3GB=$(LIN_REG_INPUTS)/key_file_3GB.txt

.PHONY: install
install: pinthreads \
         linear_regression \
         $(LIN_REG_INPUT_3GB)

.PHONY: pinthreads
pinthreads:
	make -C$(PINTHREADS_SRC_DIR)

.PHONY: linear_regression
linear_regression:
	make -C$(LIN_REG_SRC_DIR)

$(LIN_REG_INPUTS_ARCHIVE):
	mkdir -p tmp/
	@echo "### Downloading input for lin_reg (can take a while)"
	wget $(LIN_REG_INPUTS_URL) -O $(LIN_REG_INPUTS_ARCHIVE)

$(LIN_REG_INPUT_500MB): $(LIN_REG_INPUTS_ARCHIVE)
	mkdir -p $(LIN_REG_INPUTS)
	tar -xzf $(LIN_REG_INPUTS_ARCHIVE) -C $(LIN_REG_INPUTS) \
	    --strip-components 1 \
	    linear_regression_datafiles/key_file_500MB.txt
	touch $(LIN_REG_INPUT_500MB)

$(LIN_REG_INPUT_3GB): $(LIN_REG_INPUT_500MB)
	mkdir -p $(LIN_REG_INPUTS)
	for i in $$(seq 1 6); do \
	  cat $(LIN_REG_INPUT_500MB) >> $(LIN_REG_INPUT_3GB); \
	done

.PHONY: clean
clean:
	rm -rf tmp
	make -C$(PINTHREADS_SRC_DIR) clean
	make -C$(LIN_REG_SRC_DIR) clean

