#version 400 core

// in vec3 colour;
in vec2 pass_textureCoords;
// out vec4 out_Color;

uniform sampler2D textureSampler;

void main(void) {
    // out_Color = vec4(colour,1.0);
    vec4 textureColor = texture(textureSampler,pass_textureCoords);

    if (textureColor.a < 0.5) {
        discard;
    }

    float percent_grayscale = 0.5;
    float delta = (textureColor.r + textureColor.g + textureColor.b) / 3;

    textureColor.r = delta * percent_grayscale + (1-percent_grayscale) * textureColor.r;
    textureColor.g = delta * percent_grayscale + (1-percent_grayscale) * textureColor.g;
    textureColor.b = delta * percent_grayscale + (1-percent_grayscale) * textureColor.b;

    gl_FragColor = vec4(vec3(textureColor.r,textureColor.g,textureColor.b),0.75);
}