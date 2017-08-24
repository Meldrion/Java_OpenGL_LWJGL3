#version 400 core

// in vec3 colour;
in vec2 pass_textureCoords;
uniform float greyscale;
// out vec4 out_Color;

uniform sampler2D textureSampler;

void main(void) {
    // out_Color = vec4(colour,1.0);
    vec4 textureColor = texture(textureSampler,pass_textureCoords);

    if (textureColor.a < 0.5) {
        discard;
    }

    float delta = (textureColor.r + textureColor.g + textureColor.b) / 3;

    textureColor.r = delta * greyscale + (1-greyscale) * textureColor.r;
    textureColor.g = delta * greyscale + (1-greyscale) * textureColor.g;
    textureColor.b = delta * greyscale + (1-greyscale) * textureColor.b;

    gl_FragColor = vec4(vec3(textureColor.r,textureColor.g,textureColor.b),1.0);
}