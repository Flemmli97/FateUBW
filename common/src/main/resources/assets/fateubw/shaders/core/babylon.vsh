#version 150

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform sampler2D Sampler1;

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in float Time;

out vec4 vertexColor;
out vec2 texCoord0;
out float tickTime;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexColor = Color;
    texCoord0 = UV0;
    tickTime = Time;
}
