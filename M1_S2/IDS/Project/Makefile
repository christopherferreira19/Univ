

PROTO_DIR="proto"
PROTO_PYTHON_OUT_DIR="test"

test/proto/%_pb2.py: $PROTO_DIR/%.proto
	protoc userMessage.proto --python_out=$PROTO_PYTHON_OUT_DIR
