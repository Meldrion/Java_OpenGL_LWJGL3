#version 400 core

in vec3 position;
in vec2 textureCoords;
uniform mat4 transformationMatrix;
out vec2 pass_textureCoords;

void main(void)
{
	// gl_Position = transformationMatrix * vec4(pos.xy,position.z, 1.0);
	vec4 pos = transformationMatrix * vec4(position, 1.0);
	//pos = vec4(pos.x-1,pos.y+1,pos.z,1.0);
	gl_Position = pos;
	pass_textureCoords = textureCoords;
}