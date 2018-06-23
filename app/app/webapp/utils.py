from django.template import Context, Template
import logging


def tempate_to_string(template_str, context):
    template = Template(template_str)
    context = Context(context)
    template_output = template.render(context)
    logging.info(template_output)
    return template_output


def process_seo_headings(heading, context):
    heading['h1_template'] = tempate_to_string(heading['h1_template'], context)
    heading['h2_template'] = tempate_to_string(heading['h2_template'], context)
    return heading


def process_seo_data(data, context):
    process_seo_headings(data['heading_template'], context)
    process_seo_components(data['component_group']['components'], context)
    process_header_meta(data['header_seo_component'], context)
    return data


def process_seo_components(components, context):
    for component in components:        
        component['heading'] = tempate_to_string(component['heading'], context)
        component['content'] = tempate_to_string(component['content'], context)
    return components


def process_header_meta(header, context):
    
    header['title'] = tempate_to_string(header['title'], context)
    header['meta_keywords'] = tempate_to_string(
        header['meta_keywords'], context)
    header['meta_description'] = tempate_to_string(
        header['meta_description'], context)
    header['canonical_url'] = tempate_to_string(
        header['canonical_url'], context)
    return header
