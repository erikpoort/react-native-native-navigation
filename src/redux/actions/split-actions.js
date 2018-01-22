export const TOGGLE = "toggle";

export const changeAxis = (axis) => {
	return {
		type: TOGGLE,
		axis: axis,
	}
};