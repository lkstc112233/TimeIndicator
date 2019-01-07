attribute vec3 aPos;

uniform mat4 transform;
uniform mat4 view;
uniform mat4 projection;

varying vec4 color;

void main() {
    gl_Position = projection * view * transform * vec4(aPos, 1.0);
    vec3 position = aPos;
    position += vec3(1, 1, 1);
    position /= vec3(2, 2, 2);
    position = clamp(position, vec3(0, 0, 0), vec3(1, 1, 1));
    color = vec4(position, 1);
}
