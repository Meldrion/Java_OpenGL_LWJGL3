#version 400 core

in vec3 position;
in vec2 textureCoords;
uniform mat4 transformationMatrix;
uniform vec4 uvCoordinates;
out vec2 pass_textureCoords;

void main(void)
{
    vec4 pos = transformationMatrix * vec4(position, 1.0);
	gl_Position = pos;

    float uDelta = uvCoordinates.z - uvCoordinates.x;
    float vDelta = uvCoordinates.w - uvCoordinates.y;

	pass_textureCoords = vec2(textureCoords.x * uDelta + uvCoordinates.x,
	                          textureCoords.y * vDelta + uvCoordinates.y);
}