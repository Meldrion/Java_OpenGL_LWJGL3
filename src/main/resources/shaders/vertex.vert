#version 400 core

in vec3 position;
in vec2 textureCoords;
uniform mat4 transformationMatrix;
out vec2 pass_textureCoords;

void main(void)
{
    vec4 pos = transformationMatrix * vec4(position, 1.0);
	gl_Position = pos;
	pass_textureCoords = textureCoords;
}