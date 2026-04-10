export const parseCategories = (categoriesString) => {
    if (!categoriesString) return [];
    return categoriesString
    .split(';')
    .filter(segment => segment.trim() !== '')
    .map(segment => segment.trim());
}